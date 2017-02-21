package jcontext.connection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jcontext.api.command.Command;
import jcontext.api.handler.ApiDispatcher;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class ServerConnectionModule extends AbstractModule {
    public static final String SERVER_PORT = System.getProperty("server.port", "24443");

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(ServerConnectionResponder.Factory.class));
        install(new FactoryModuleBuilder().build(ApiDispatcher.Factory.class));
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
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(inboundHandler);
    }

    @Provides
    ChannelInboundHandler getInboundHandler(ServerConnectionResponder.Factory responderFactory,
                                            ApiDispatcher.Factory apiDispatcherFactory) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                InetSocketAddress remoteAddress = channel.remoteAddress();
                log.info("Initialized connection with {}", remoteAddress);

                ApiDispatcher apiDispatcher = apiDispatcherFactory.create(responderFactory.create(channel));

                channel.pipeline()
                        .addLast(new ObjectEncoder())
                        .addLast(new ObjectDecoder(ClassResolvers.softCachingResolver(null)))
                        .addLast(new SimpleChannelInboundHandler<Command>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Command command) throws Exception {
                                apiDispatcher.handleCommand(command);
                            }
                        });

                channel.closeFuture().addListener(future -> log.info("Closed connection with {}", remoteAddress));
            }
        };
    }
}
