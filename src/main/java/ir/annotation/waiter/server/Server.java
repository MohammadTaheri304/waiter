package ir.annotation.waiter.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ir.annotation.waiter.core.application.Component;
import ir.annotation.waiter.server.handler.Initializer;
import ir.annotation.waiter.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static ir.annotation.waiter.util.OSUtil.OS.*;

/**
 * A netty based server socket implementation.
 *
 * @author Alireza Pourtaghi
 */
public final class Server extends Component<Server, ChannelFuture> {
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
    private EventLoopGroup eventLoopGroup;

    /**
     * Host value that this server must listen on.
     * <p>
     * The default value is listening on localhost.
     * </p>
     */
    private String host;

    /**
     * Port number that this server must listen on.
     * <p>
     * The default value is listening on 6666.
     * </p>
     */
    private int port;

    /**
     * Maximum amount of read size in kilo bytes.
     * <p>
     * Default value is 1024 kb.
     * </p>
     */
    private int maxReadSize;

    /**
     * Maximum amount of individual read size in kilo bytes.
     * <p>
     * Default value is 1 kb.
     * </p>
     */
    private int maxIndividualReadSize;

    /**
     * Public accessible constructor to identify this component.
     */
    public Server() {
        super("server");
    }

    /**
     * Private constructor to build an instance of this server implementation.
     *
     * @param eventLoopGroup        Event loop group that must be assigned to netty server for handling IO operations.
     * @param host                  Host value that this server must listen on.
     * @param port                  Port number that this server must listen on.
     * @param maxReadSize           Maximum amount of read size in kilo bytes.
     * @param maxIndividualReadSize Maximum amount of individual read size in kilo bytes.
     */
    private Server(EventLoopGroup eventLoopGroup, String host, int port, int maxReadSize, int maxIndividualReadSize) {
        this();
        this.eventLoopGroup = eventLoopGroup;
        this.host = host;
        this.port = port;
        this.maxReadSize = maxReadSize;
        this.maxIndividualReadSize = maxIndividualReadSize;
    }

    /**
     * Setups the server.
     *
     * @param properties Application properties.
     * @return A newly created and ready to start {@link Server}.
     */
    public Server setup(Properties properties) {
        var os = OSUtil.detectOS();

        var eventLoopGroup = os.equals(LINUX) ? new EpollEventLoopGroup() : os.equals(OSX) || os.equals(BSD) ? new KQueueEventLoopGroup() : new NioEventLoopGroup();
        var host = properties.getOrDefault("server.host", "0.0.0.0").toString();
        var port = Integer.parseInt(properties.getOrDefault("server.port", "6666").toString());
        var maxReadSize = Integer.parseInt(properties.getOrDefault("server.max-read-size.kb", "1024").toString());
        var maxIndividualReadSize = Integer.parseInt(properties.getOrDefault("server.max-individual-read-size.kb", "1").toString());

        return new Server(eventLoopGroup, host, port, maxReadSize, maxIndividualReadSize);
    }

    /**
     * Starts the server.
     *
     * @return Returned value is a {@link ChannelFuture} that is the result of bind call on server bootstrap.
     * @throws InterruptedException If any exception occurred during bind operation.
     */
    public ChannelFuture start() throws InterruptedException {
        logger.info("starting server on {}:{} ...", getHost(), getPort());
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
        serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new DefaultMaxBytesRecvByteBufAllocator(getMaxReadSize() * 1024, getMaxIndividualReadSize() * 1024));
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

    private String getHost() {
        return host;
    }

    private int getPort() {
        return port;
    }

    private int getMaxReadSize() {
        return maxReadSize;
    }

    private int getMaxIndividualReadSize() {
        return maxIndividualReadSize;
    }
}
