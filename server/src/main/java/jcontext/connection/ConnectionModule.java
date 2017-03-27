package jcontext.connection;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
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
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jcontext.api.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ConnectionModule extends AbstractModule {
    public static final String SERVER_PORT = System.getProperty("server.port", "24443");
    public static final String HTTP_SERVER_PORT = System.getProperty("http.server.port", "8080");

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(ServerConnectionResponder.Factory.class));
        install(new FactoryModuleBuilder().build(HttpServerConnectionResponder.Factory.class));
        install(new FactoryModuleBuilder().build(ApiDispatcher.Factory.class));
    }

    @Provides
    List<Connection> getConnections(ServerConnection serverConnection, HttpServerConnection httpServerConnection) {
        return ImmutableList.of(serverConnection, httpServerConnection);
    }

    @Provides @Named("server.port")
    int getPort() {
        return Integer.parseInt(SERVER_PORT);
    }

    @Provides @Named("http.server.port")
    public int getHttpServerPort() {
        return Integer.parseInt(HTTP_SERVER_PORT);
    }

    @Provides
    @Named("serverBootstrap")
    ServerBootstrap getServerBootstrap(@Named("inboundHandler") ChannelInboundHandler inboundHandler) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        return bootstrap.channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup())
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(inboundHandler);
    }

    @Provides
    @Named("httpServerBootstrap")
    ServerBootstrap getHttpServerBootstrap(@Named("httpInboundHandler") ChannelInboundHandler inboundHandler) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        return bootstrap.channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup())
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(inboundHandler);
    }

    @Provides
    @Named("inboundHandler")
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

    @Provides
    @Named("httpInboundHandler")
    ChannelInboundHandler getHttpInboundHandler(HttpServerConnectionResponder.Factory responderFactory,
                                                ApiDispatcher.Factory apiDispatcherFactory,
                                                Gson gson) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                InetSocketAddress remoteAddress = channel.remoteAddress();
                log.info("Initialized http connection with {}", remoteAddress);

                ApiDispatcher apiDispatcher = apiDispatcherFactory.create(responderFactory.create(channel));

                channel.pipeline()
                        .addLast(new HttpResponseEncoder())
                        .addLast(new HttpRequestDecoder())
                        .addLast(new HttpObjectAggregator(1048576))
                        .addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
                                Class<? extends Command> commandClass = getCommandForUri(msg.uri());
                                Command command;

                                if (commandClass != null && (command = gson.fromJson(msg.content().toString(Charsets.UTF_8), commandClass)) != null) {
                                    apiDispatcher.handleCommand(command);
                                } else {
                                    ctx.writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
                                    ctx.close();
                                }
                            }
                        });

                channel.closeFuture().addListener(future -> log.info("Closed http connection with {}", remoteAddress));
            }
        };
    }

    private Class<? extends Command> getCommandForUri(String uri) {
        Reflections reflections = new Reflections("jcontext");

        Map<String, Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class).stream()
                .collect(Collectors.toMap(Class::getSimpleName, Function.identity()));

        return commandClasses.get(Lists.newArrayList(uri.split("/")).stream()
                .filter(value -> !Strings.isNullOrEmpty(value))
                .map(value -> value.substring(0, 1).toUpperCase().concat(value.substring(1)))
                .reduce("", String::concat).concat("Command"));
    }
}
