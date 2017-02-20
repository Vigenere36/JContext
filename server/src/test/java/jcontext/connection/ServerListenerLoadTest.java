package jcontext.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jcontext.ServerTestHarness;
import jcontext.ServerTestHarnessModule;
import jcontext.ServerTestStat;
import jcontext.api.command.TestCommand;
import jcontext.api.response.ResponseType;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServerListenerLoadTest {
    private ServerTestStat stat = new ServerTestStat(TimeUnit.NANOSECONDS);
    private ServerTestHarness harness;

    @Before
    public void initialize() {
        Injector injector = Guice.createInjector(new ServerTestHarnessModule());
        harness = injector.getInstance(ServerTestHarness.class);
    }

    @Test
    public void loadTest() throws InterruptedException, ExecutionException, TimeoutException {
        for (int i = 1; i <= 10000; i *= 10) {
            sendFast(i, false);
            stat.reset();
        }
    }

    @Test
    public void burstTest() throws InterruptedException, ExecutionException, TimeoutException {
        for (int i = 1; i <= 1000000; i *= 10) {
            sendFast(i, true);
            stat.reset();
        }
    }

    private void sendFast(int iterations, boolean burst) throws InterruptedException, TimeoutException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(iterations);
        TestCommand testCommand = new TestCommand();

        for (long i = 0; i < iterations; i++) {
            long tag = i;
            stat.start(tag);
            harness.send(testCommand, response -> {
                stat.end(tag);
                latch.countDown();
                return response.responseType() == ResponseType.ACK;
            }, 200);

            if (!burst) Thread.sleep(0, 500);
        }

        latch.await();
        stat.printStats();
    }
}
