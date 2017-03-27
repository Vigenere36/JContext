package jcontext.connection;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class HttpServerConnection implements Connection {
    private final int httpServerPort;
    private final ServerBootstrap serverBootstrap;

    @Inject
    HttpServerConnection(@Named("http.server.port") int httpServerPort,
                         @Named("httpServerBootstrap") ServerBootstrap serverBootstrap) {
        this.httpServerPort = httpServerPort;
        this.serverBootstrap = serverBootstrap;
    }

    @Override
    public ChannelFuture listen() throws InterruptedException {
        ChannelFuture future = serverBootstrap.bind(httpServerPort).sync();
        log.info("Listening for incoming http connections on port {}", httpServerPort);
        return future.channel().closeFuture();
    }
}
