package jcontext.connection;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ServerConnection implements Connection {
    private final int serverPort;
    private final ServerBootstrap serverBootstrap;

    @Inject
    ServerConnection(@Named("server.port") int serverPort,
                     @Named("serverBootstrap") ServerBootstrap serverBootstrap) {
        this.serverPort = serverPort;
        this.serverBootstrap = serverBootstrap;
    }

    @Override
    public ChannelFuture listen() throws InterruptedException {
        ChannelFuture future = serverBootstrap.bind(serverPort).sync();

        log.info("Listening for incoming connections on port {}", serverPort);
        return future.channel().closeFuture();
    }
}
