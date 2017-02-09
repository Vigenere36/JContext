package jcontext.connection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;
import jcontext.api.ApiDispatcher;
import jcontext.api.command.Command;

public class ServerConnectionModule extends AbstractModule {
    private static final String SERVER_PORT = System.getProperty("server.port", "24443");

    @Override
    protected void configure() {
    }

    @Provides @Named("server.port")
    int getPort() {
        return Integer.parseInt(SERVER_PORT);
    }

    @Provides
    ServerBootstrap getServerBootstrap(ChannelInboundHandler inboundHandler) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        return bootstrap.channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup())
                .handler(new LoggingHandler())
                .childHandler(inboundHandler);
    }

    @Provides
    ChannelInboundHandler getInboundHandler(ApiDispatcher apiDispatcher) {
        return new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline()
                        .addLast(new ObjectEncoder())
                        .addLast(new ObjectDecoder(ClassResolvers.softCachingResolver(null)))
                        .addLast(new SimpleChannelInboundHandler<Command>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Command command) throws Exception {
                                apiDispatcher.handleCommand(command);
                            }
                        });
            }
        };
    }
}
