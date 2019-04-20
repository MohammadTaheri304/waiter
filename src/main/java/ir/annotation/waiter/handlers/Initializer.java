package ir.annotation.waiter.handlers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * A socket channel initializer implementation. This implementation is sharable.
 *
 * @author Alireza Pourtaghi
 */
@Sharable
public class Initializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // TODO: Complete channel initialization phase.
        socketChannel.pipeline().addLast();
    }
}
