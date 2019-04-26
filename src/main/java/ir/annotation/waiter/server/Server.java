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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    /**
     * Host value that this server must listen on.
     * <p>
     * The default value is listening on localhost.
     * </p>
     */
    private String host = "localhost";

    /**
     * Port number that this server must listen on.
     * <p>
     * The default value is listening on 6666.
     * </p>
     */
    private int port = 6666;

    /**
     * Default private empty constructor.
     */
    private Server() {
    }

    /**
     * Starts the server with all default values.
     *
     * @return A newly created and ready to start {@link Server}.
     */
    public static Server setup() {
        return new Server();
    }

    /**
     * Setups the server with host and port as default values.
     *
     * @param eventLoopGroup Event loop group for netty server.
     * @return A newly created and ready to start {@link Server}.
     * @throws IllegalArgumentException If provided event loop group is not supported.
     */
    public static Server setup(EventLoopGroup eventLoopGroup) {
        var server = new Server();

        if (!(eventLoopGroup instanceof NioEventLoopGroup) && !(eventLoopGroup instanceof EpollEventLoopGroup) && !(eventLoopGroup instanceof KQueueEventLoopGroup))
            throw new IllegalArgumentException("Unsupported event loop group type.");
        server.setEventLoopGroup(eventLoopGroup);

        return server;
    }

    /**
     * Setups the server with event loop group as default value.
     *
     * @param host Host value that the server must listen on.
     * @param port Port number that the server must listen on.
     * @return A newly created and ready to start {@link Server}.
     */
    public static Server setup(String host, int port) {
        var server = new Server();

        server.setHost(host);
        server.setPort(port);

        return server;
    }

    /**
     * Setups the server.
     *
     * @param eventLoopGroup Event loop group for netty server.
     * @param host           Host value that the server must listen on.
     * @param port           Port number that the server must listen on.
     * @return A newly created and ready to start {@link Server}.
     */
    public static Server setup(EventLoopGroup eventLoopGroup, String host, int port) {
        var server = new Server();

        server.setEventLoopGroup(eventLoopGroup);
        server.setHost(host);
        server.setPort(port);

        return server;
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
        if (getEventLoopGroup() instanceof NioEventLoopGroup)
            serverBootstrap.channel(NioServerSocketChannel.class);
        else if (getEventLoopGroup() instanceof EpollEventLoopGroup)
            serverBootstrap.channel(EpollServerSocketChannel.class);
        else if (getEventLoopGroup() instanceof KQueueEventLoopGroup)
            serverBootstrap.channel(KQueueServerSocketChannel.class);
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

    private EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    private void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    public String getHost() {
        return host;
    }

    private void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }
}
