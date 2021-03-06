package jcontext.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jcontext.ServerTestHarness;
import jcontext.ServerTestHarnessModule;
import jcontext.api.command.CreateBoardCommand;
import jcontext.api.response.ResponseType;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ServerListenerStackTest {
    private static final Logger log = LoggerFactory.getLogger(ServerListenerStackTest.class);
    private ServerTestHarness harness;

    @Before
    public void initialize() {
        Injector injector = Guice.createInjector(new ServerTestHarnessModule());
        harness = injector.getInstance(ServerTestHarness.class);
    }

    @Test
    public void testCreateBoard() throws InterruptedException, ExecutionException, TimeoutException {
        harness.send(new CreateBoardCommand("Ｒｅ：ゼロから始める異世界生活"),
                response -> response.responseType() == ResponseType.ACK, 1000);
    }
}