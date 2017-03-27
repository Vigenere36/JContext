package jcontext;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import jcontext.connection.ConnectionModule;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ServerTestHarnessModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PredicatedResponseHandler.class).toInstance(new PredicatedResponseHandler());
    }

    @Provides
    public Bootstrap getClientBootstrap() {
        return new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    @Provides
    public SocketAddress getServerAddress() {
        return new InetSocketAddress("localhost", Integer.parseInt(ConnectionModule.SERVER_PORT));
    }
}
