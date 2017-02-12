package jcontext;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import jcontext.api.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ServerTestChannelHandler extends ChannelInitializer<SocketChannel> {
    private static final Logger log = LoggerFactory.getLogger(ServerTestChannelHandler.class);

    interface Factory {
        ServerTestChannelHandler create(ServerTestResponseHandler responseHandler);
    }

    private final ServerTestResponseHandler responseHandler;

    @Inject
    ServerTestChannelHandler(@Assisted ServerTestResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new ObjectEncoder())
                .addLast(new ObjectDecoder(ClassResolvers.softCachingResolver(null)))
                .addLast(new SimpleChannelInboundHandler<Response>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
                        log.info("Read from channel");
                        responseHandler.handleResponse(msg);
                    }
                });
    }
}