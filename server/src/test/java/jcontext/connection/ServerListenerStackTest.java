package jcontext.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jcontext.JContextModule;
import jcontext.ServerTestHarness;
import jcontext.ServerTestHarnessModule;
import jcontext.api.command.CreateBoardCommand;
import jcontext.api.response.ResponseType;
import org.junit.Before;
import org.junit.Test;

public class ServerListenerStackTest {
    private ServerTestHarness harness;

    @Before
    public void initialize() {
        Injector injector = Guice.createInjector(new ServerTestHarnessModule());
        harness = injector.getInstance(ServerTestHarness.class);
    }

    @Test
    public void testCreateBoard() throws InterruptedException {
        harness.send(new CreateBoardCommand("Ｒｅ：ゼロから始める異世界生活"),
                response -> response.responseType() == ResponseType.ACK);
    }
}