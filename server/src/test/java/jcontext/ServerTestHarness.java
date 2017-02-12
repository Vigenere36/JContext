package jcontext;

import com.google.inject.Inject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class ServerTestHarness {
    private static final Logger log = LoggerFactory.getLogger(ServerTestHarness.class);

    private final ServerTestChannelHandler.Factory channelHandlerFactory;
    private final Bootstrap clientBootstrap;
    private final SocketAddress remoteAddress;

    @Inject
    ServerTestHarness(ServerTestChannelHandler.Factory channelHandlerFactory, Bootstrap clientBootstrap,
                      SocketAddress remoteAddress) {
        this.channelHandlerFactory = channelHandlerFactory;
        this.clientBootstrap = clientBootstrap;
        this.remoteAddress = remoteAddress;
    }

    public Channel connect(ServerTestResponseHandler responseHandler) throws InterruptedException {
        log.info("Attempting to connect at address {}", remoteAddress);

        ChannelFuture channelFuture = clientBootstrap.handler(channelHandlerFactory.create(responseHandler))
                .connect(remoteAddress)
                .addListener(future -> log.info("Connection to {} successful: {}", remoteAddress, future.isSuccess()))
                .awaitUninterruptibly();

        return channelFuture.channel();
    }
}
