package ir.annotation.waiter.server.handler.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import ir.annotation.waiter.server.Error;
import org.msgpack.core.MessagePack;

import java.util.List;

/**
 * Message pack decoder and validator.
 *
 * @author Alireza Pourtaghi
 */
public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        var readableBytes = in.readableBytes();
        var array = new byte[readableBytes];
        in.readBytes(array);

        try (var buffer = MessagePack.newDefaultUnpacker(array)) {
            var message = buffer.unpackValue();
            if (!message.isMapValue())
                ctx.fireExceptionCaught(Error.Reason.INVALID_MESSAGE_FORMAT.getError());

            out.add(message);
        }
    }
}
