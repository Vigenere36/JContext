package jcontext;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.SettableFuture;
import jcontext.api.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class PredicatedResponseHandler {
    private static final Logger log = LoggerFactory.getLogger(PredicatedResponseHandler.class);
    private final Multimap<Predicate<Response>, SettableFuture<Boolean>> predicates = ArrayListMultimap.create();

    void handleResponse(Response response) {
        predicates.keySet().stream()
                .filter(predicate -> predicate.test(response))
                .map(predicates::get)
                .collect(Collectors.toList())
                .forEach(futures -> futures.forEach(future -> future.set(true)));
    }

    Future<Boolean> expect(Predicate<Response> predicate) {
        SettableFuture<Boolean> future = SettableFuture.create();
        predicates.put(predicate, future);
        future.addListener(() -> {
            log.info("Predicate completed");
            predicates.remove(predicate, future);
        }, Executors.newCachedThreadPool());
        return future;
    }
}
