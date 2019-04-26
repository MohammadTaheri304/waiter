package ir.annotation.waiter.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ir.annotation.waiter.server.exceptions.Error;
import org.msgpack.core.MessagePack;
import org.msgpack.value.Value;

import static io.netty.channel.ChannelHandler.Sharable;
import static ir.annotation.waiter.utils.MessagePackUtil.*;

/**
 * Channel inbound error handler to handle all kind of errors.
 *
 * @author Alireza Pourtaghi
 */
@Sharable
public class ChannelInboundErrorHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof Error) {
            try {
                try (var buffer = MessagePack.newDefaultBufferPacker()) {
                    buffer.packValue(buildErrorMessage((Error) cause));
                    var bytesOut = ctx.alloc().directBuffer((int) buffer.getTotalWrittenBytes());
                    bytesOut.writeBytes(buffer.toByteArray());
                    ctx.writeAndFlush(bytesOut);
                }
            } finally {
                ctx.close();
            }
        } else {
            ctx.fireExceptionCaught(cause);
        }
    }

    /**
     * Generates appropriate binary message.
     *
     * @param error The {@link Error} that should be converted to binary message.
     * @return Message pack's {@link Value} format.
     */
    private Value buildErrorMessage(Error error) {
        return array(map(
                string("code"), string(error.getCode()),
                string("message"), string(error.getMessage())
        ));
    }
}
