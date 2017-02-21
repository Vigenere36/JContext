package jcontext.connection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.netty.channel.Channel;
import jcontext.api.response.Response;

public class ServerConnectionResponder {
    public interface Factory {
        ServerConnectionResponder create(Channel channel);
    }

    private final Channel channel;

    @Inject
    public ServerConnectionResponder(@Assisted Channel channel) {
        this.channel = channel;
    }

    public void sendResponse(Response response) {
        channel.writeAndFlush(response);
    }
}
