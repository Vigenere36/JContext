package jcontext.connection;

import io.netty.channel.ChannelFuture;

public interface Connection {
    /**
     * Listens for incoming connections
     * Returns a closeFuture, to block on it call sync()
     */
    ChannelFuture listen() throws InterruptedException;
}
