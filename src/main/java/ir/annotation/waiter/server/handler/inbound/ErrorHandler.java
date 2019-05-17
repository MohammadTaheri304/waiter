package ir.annotation.waiter.server.handler.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ir.annotation.waiter.server.Error;
import org.msgpack.core.MessagePack;
import org.msgpack.value.Value;

import static io.netty.channel.ChannelHandler.Sharable;
import static ir.annotation.waiter.util.MessagePackUtil.*;

/**
 * Channel inbound error handler to handle all kind of errors.
 *
 * @author Alireza Pourtaghi
 */
@Sharable
public class ErrorHandler extends ChannelInboundHandlerAdapter {

    public ErrorHandler() {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            if (cause instanceof Error) {
                try (var buffer = MessagePack.newDefaultBufferPacker()) {
                    buffer.packValue(buildErrorMessage((Error) cause));
                    var bytesOut = ctx.alloc().buffer((int) buffer.getTotalWrittenBytes()); // Default to allocate direct buffer.
                    bytesOut.writeBytes(buffer.toByteArray());
                    ctx.writeAndFlush(bytesOut);
                }
            } else {
                ctx.fireExceptionCaught(cause);
            }
        } finally {
            ctx.close();
        }
    }

    /**
     * Generates appropriate binary message.
     *
     * @param error The {@link Error} that should be converted to binary message.
     * @return Message pack's {@link Value} format.
     */
    private Value buildErrorMessage(Error error) {
        return map(
                string("succ"), bool(false),
                string("errs"), array(map(
                        string("code"), string(error.getCode()),
                        string("mess"), string(error.getMessage())
                ))
        );
    }
}
