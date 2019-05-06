package ir.annotation.waiter.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ir.annotation.waiter.server.handlers.Initializer;
import ir.annotation.waiter.utils.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static ir.annotation.waiter.utils.OSUtil.OS.*;

/**
 * A netty based server socket implementation.
 *
 * @author Alireza Pourtaghi
 */
public final class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    /**
     * A default socket channel initializer to initialize server side channels.
     */
    private static final ChannelInitializer<SocketChannel> channelInitializer = new Initializer();

    /**
     * Event loop group that must be assigned to netty server for handling IO operations.
     * <p>
     * The default value of event loop group is an NIO based event loop group.
     * </p>
     */
    private final EventLoopGroup eventLoopGroup;

    /**
     * Host value that this server must listen on.
     * <p>
     * The default value is listening on localhost.
     * </p>
     */
    private final String host;

    /**
     * Port number that this server must listen on.
     * <p>
     * The default value is listening on 6666.
     * </p>
     */
    private final int port;

    /**
     * Default private constructor.
     */
    public Server(EventLoopGroup eventLoopGroup, String host, int port) {
        this.eventLoopGroup = eventLoopGroup;
        this.host = host;
        this.port = port;
    }

    /**
     * Setups the server.
     *
     * @param properties Application properties.
     * @return A newly created and ready to start {@link Server}.
     */
    public static Server setup(Properties properties) {
        var os = OSUtil.detectOS();

        var eventLoopGroup = os.equals(LINUX) ? new EpollEventLoopGroup() : os.equals(OSX) || os.equals(BSD) ? new KQueueEventLoopGroup() : new NioEventLoopGroup();
        var host = properties.getOrDefault("server.host", "0.0.0.0").toString();
        var port = Integer.parseInt(properties.getOrDefault("server.port", "6666").toString());

        return new Server(eventLoopGroup, host, port);
    }

    /**
     * Starts the server.
     *
     * @return Returned value is a {@link ChannelFuture} that is the result of bind call on server bootstrap.
     * @throws InterruptedException If any exception occurred during bind operation.
     */
    public ChannelFuture start() throws InterruptedException {
        logger.info("starting server ...");
        var serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(getEventLoopGroup());
        if (getEventLoopGroup() instanceof NioEventLoopGroup) {
            serverBootstrap.channel(NioServerSocketChannel.class);
        } else if (getEventLoopGroup() instanceof EpollEventLoopGroup) {
            logger.info("using epoll native transport");
            serverBootstrap.channel(EpollServerSocketChannel.class);
        } else if (getEventLoopGroup() instanceof KQueueEventLoopGroup) {
            logger.info("using kqueue native transport");
            serverBootstrap.channel(KQueueServerSocketChannel.class);
        }
        serverBootstrap.localAddress(getHost(), getPort());
        serverBootstrap.childHandler(channelInitializer);

        return serverBootstrap.bind().sync();
    }

    /**
     * Tries to gracefully shutdown the server.
     *
     * @throws InterruptedException If any exception occurred during shutdown operation.
     */
    public void stop() throws InterruptedException {
        logger.info("stopping server ...");
        eventLoopGroup.shutdownGracefully().sync();
    }

    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
