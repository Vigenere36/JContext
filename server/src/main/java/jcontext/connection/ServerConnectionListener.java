package jcontext.connection;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerConnectionListener {
    private final int serverPort;
    private final ServerBootstrap serverBootstrap;

    @Inject
    ServerConnectionListener(@Named("server.port") int serverPort, ServerBootstrap serverBootstrap) {
        this.serverPort = serverPort;
        this.serverBootstrap = serverBootstrap;
    }

    public void listen() throws InterruptedException {
        ChannelFuture future = serverBootstrap.bind(serverPort).sync();

        log.info("Listening for incoming connections on port {}", serverPort);
        future.channel().closeFuture().sync();
    }
}
