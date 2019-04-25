package ir.annotation.waiter.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * Channel inbound exception handler that is inserted as last channel handler in channel pipeline.
 * <p>
 * This sharable class is a general exception handler that logs the exception and closes the channel.
 * </p>
 *
 * @author Alireza Pourtaghi
 */
@Sharable
public class ChannelInboundExceptionHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ChannelInboundExceptionHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exception caught ", cause);
        ctx.close();
    }
}
