package jcontext;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.SettableFuture;
import jcontext.api.response.Response;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.function.Predicate;

class PredicatedResponseHandler {
    private final Multimap<Predicate<Response>, SettableFuture<Response>> predicates =
            Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
    private final Semaphore semaphore = new Semaphore(1);

    void handleResponse(Response response) throws InterruptedException {
        semaphore.acquire();

        Sets.newHashSet(predicates.keySet())
                .stream()
                .filter(predicate -> predicate.test(response))
                .map(predicates::removeAll)
                .flatMap(Collection::stream)
                .forEach(future -> future.set(response));

        semaphore.release();
    }

    synchronized Future<Response> expect(Predicate<Response> predicate) throws InterruptedException {
        SettableFuture<Response> future = SettableFuture.create();

        semaphore.acquire();
        predicates.put(predicate, future);
        semaphore.release();

        return future;
    }
}
