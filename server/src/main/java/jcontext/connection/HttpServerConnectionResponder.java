package jcontext.connection;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import jcontext.api.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class HttpServerConnectionResponder implements ConnectionResponder {
    interface Factory {
        HttpServerConnectionResponder create(Channel channel);
    }

    private final Channel channel;
    private final Gson gson;

    @Inject
    HttpServerConnectionResponder(@Assisted Channel channel, Gson gson) {
        this.channel = channel;
        this.gson = gson;
    }

    @Override
    public void sendResponse(Response response) {
        log.info("Sending http json response: {}", gson.toJson(response));

        ByteBuf responseBuffer = Unpooled.wrappedBuffer(gson.toJson(response).getBytes());
        channel.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, responseBuffer));
        channel.close();
    }
}
