package jcontext;

import com.google.inject.Inject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import jcontext.api.command.Command;
import jcontext.api.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

public class ServerTestHarness {
    private static final Logger log = LoggerFactory.getLogger(ServerTestHarness.class);

    private final ServerTestChannelHandler channelHandler;
    private final Bootstrap clientBootstrap;
    private final SocketAddress remoteAddress;
    private final PredicatedResponseHandler responseHandler;

    private Channel channel;

    @Inject
    ServerTestHarness(ServerTestChannelHandler channelHandler, Bootstrap clientBootstrap,
                      SocketAddress remoteAddress, PredicatedResponseHandler responseHandler) throws InterruptedException {
        this.channelHandler = channelHandler;
        this.clientBootstrap = clientBootstrap;
        this.remoteAddress = remoteAddress;
        this.responseHandler = responseHandler;

        connectToServer();
    }

    public void send(Command command) {
        if (channel == null) throw new IllegalStateException("Connection not initialized");
        channel.writeAndFlush(command);
    }

    public void send(Command command, Predicate<Response> predicate, long timeoutMillis) throws InterruptedException, ExecutionException, TimeoutException {
        if (channel == null) throw new IllegalStateException("Connection not initialized");

        Future<Boolean> future = responseHandler.expect(predicate);
        send(command);
        future.get(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    private void connectToServer() throws InterruptedException {
        log.info("Attempting to connectToServer at address {}", remoteAddress);

        ChannelFuture channelFuture = clientBootstrap.handler(channelHandler).connect(remoteAddress)
                .addListener(future -> log.info("Connection to {} successful: {}", remoteAddress, future.isSuccess()))
                .awaitUninterruptibly();

        channel = channelFuture.channel();
        channel.closeFuture().addListener(future -> channel = null);
    }
}
