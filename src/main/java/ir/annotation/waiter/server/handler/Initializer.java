package ir.annotation.waiter.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import ir.annotation.waiter.server.handler.inbound.ErrorHandler;
import ir.annotation.waiter.server.handler.inbound.ExceptionHandler;
import ir.annotation.waiter.server.handler.inbound.MessageDecoder;
import org.msgpack.core.MessagePack;
import org.msgpack.value.impl.ImmutableBinaryValueImpl;

import static io.netty.channel.ChannelHandler.Sharable;
import static ir.annotation.waiter.server.util.MessagePackUtil.bytes;

/**
 * A socket channel initializer implementation. This implementation is sharable.
 *
 * @author Alireza Pourtaghi
 */
@Sharable
public class Initializer extends ChannelInitializer<SocketChannel> {
    /**
     * Delimiter bytes.
     */
    private static final ImmutableBinaryValueImpl DELIMITER_BYTES = bytes(new byte[]{'\r', '\n', '\r', '\n'});

    /**
     * Maximum amount of frame size in kilo bytes.
     */
    private final int maxFrameSize;

    /**
     * Channel inbound error handler to handle logical errors.
     */
    private final ErrorHandler errorHandler = new ErrorHandler();

    /**
     * Channel inbound exception handler as last handler in channel pipeline.
     */
    private final ExceptionHandler exceptionHandler = new ExceptionHandler();

    /**
     * Constructor to build a channel initializer.
     *
     * @param maxFrameSize Maximum amount of frame size in kilo bytes.
     */
    public Initializer(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        try (var buffer = MessagePack.newDefaultBufferPacker()) {
            buffer.packValue(DELIMITER_BYTES);
            var frameDelimiter = Unpooled.buffer((int) buffer.getTotalWrittenBytes());
            frameDelimiter.writeBytes(buffer.toByteArray());

            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(maxFrameSize * 1024, frameDelimiter));
            socketChannel.pipeline().addLast(new MessageDecoder());
            socketChannel.pipeline().addLast(errorHandler);
            socketChannel.pipeline().addLast(exceptionHandler);
        }
    }
}
