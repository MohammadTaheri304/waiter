package ir.annotation.waiter.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.msgpack.core.MessagePack;
import org.msgpack.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.channel.ChannelHandler.Sharable;
import static ir.annotation.waiter.util.MessagePackUtil.*;

/**
 * Channel inbound exception handler that is inserted as last channel handler in channel pipeline to handle all kinds of {@link Throwable}.
 * <p>
 * This sharable class is a general exception handler that logs the exception, sends back appropriate response and finally closes the channel.
 * </p>
 *
 * @author Alireza Pourtaghi
 */
@Sharable
public class ChannelInboundExceptionHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ChannelInboundExceptionHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            logger.error("exception caught ", cause);

            try (var buffer = MessagePack.newDefaultBufferPacker()) {
                buffer.packValue(internalServerErrorMessage());
                var bytesOut = ctx.alloc().directBuffer((int) buffer.getTotalWrittenBytes());
                bytesOut.writeBytes(buffer.toByteArray());
                ctx.writeAndFlush(bytesOut);
            }
        } finally {
            ctx.close();
        }
    }

    /**
     * Generates appropriate binary message.
     *
     * @return Message pack's {@link Value} format.
     */
    private Value internalServerErrorMessage() {
        return array(map(
                string("code"), string("internal.server.error"),
                string("message"), string("Internal server error.")
        ));
    }
}
