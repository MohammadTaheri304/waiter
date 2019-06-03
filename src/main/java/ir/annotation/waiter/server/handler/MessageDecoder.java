package ir.annotation.waiter.server.handler;

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

    /**
     * Empty constructor.
     */
    public MessageDecoder() {
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        var bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);

        try (var buffer = MessagePack.newDefaultUnpacker(bytes)) {
            var message = buffer.unpackValue();
            if (!message.isMapValue())
                ctx.fireExceptionCaught(Error.Reason.INVALID_MESSAGE_FORMAT.getError());
            else
                out.add(message);
        }
    }
}
