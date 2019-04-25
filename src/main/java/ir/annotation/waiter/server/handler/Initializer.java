package ir.annotation.waiter.server.handler;

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
    /**
     * Channel inbound exception handler as last handler in channel pipeline.
     */
    private final ChannelInboundExceptionHandler channelInboundExceptionHandler = new ChannelInboundExceptionHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(channelInboundExceptionHandler);
    }
}
