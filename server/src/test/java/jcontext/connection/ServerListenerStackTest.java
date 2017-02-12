package jcontext.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.netty.channel.Channel;
import jcontext.ServerTestHarness;
import jcontext.ServerTestHarnessModule;
import jcontext.api.command.CreateBoardCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerListenerStackTest {
    private static final Logger log = LoggerFactory.getLogger(ServerListenerStackTest.class);

    public static void main(String[] args) throws InterruptedException {
        Injector injector = Guice.createInjector(new ServerTestHarnessModule());

        ServerTestHarness harness = injector.getInstance(ServerTestHarness.class);
        Channel channel = harness.connect(response -> log.info("Got response of type {}", response.responseType()));

        channel.writeAndFlush(new CreateBoardCommand("Ｒｅ：ゼロから始める異世界生活"))
                .addListener(future -> log.info("Write success: {}", future.isSuccess()));
    }
}