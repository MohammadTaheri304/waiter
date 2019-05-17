package ir.annotation.waiter.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import ir.annotation.waiter.server.handler.inbound.ErrorHandler;
import ir.annotation.waiter.server.handler.inbound.ExceptionHandler;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * A socket channel initializer implementation. This implementation is sharable.
 *
 * @author Alireza Pourtaghi
 */
@Sharable
public class Initializer extends ChannelInitializer<SocketChannel> {
    /**
     * Channel inbound error handler to handle logical errors.
     */
    private final ErrorHandler errorHandler = new ErrorHandler();

    /**
     * Channel inbound exception handler as last handler in channel pipeline.
     */
    private final ExceptionHandler exceptionHandler = new ExceptionHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(errorHandler);
        socketChannel.pipeline().addLast(exceptionHandler);
    }
}
