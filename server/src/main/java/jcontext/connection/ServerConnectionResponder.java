package jcontext.connection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.netty.channel.Channel;
import jcontext.api.response.Response;

class ServerConnectionResponder {
    interface Factory {
        ServerConnectionResponder create(Channel channel);
    }

    private final Channel channel;

    @Inject
    ServerConnectionResponder(@Assisted Channel channel) {
        this.channel = channel;
    }

    void sendResponse(Response response) {
        channel.writeAndFlush(response);
    }
}
