package ir.annotation.waiter;

import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import ir.annotation.waiter.server.Server;
import ir.annotation.waiter.utils.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ir.annotation.waiter.utils.OSUtil.OS.*;

/**
 * Main application class that includes main method.
 *
 * @author Alireza Pourtaghi
 */
public final class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * Main method that is invoked by JVM to start application.
     *
     * @param args Arguments passed to the application on startup, including options and flags.
     */
    public static void main(String[] args) {
        try {
            var os = OSUtil.detectOS();
            var server = os.equals(LINUX) ? Server.setup(new EpollEventLoopGroup())
                    : os.equals(OSX) || os.equals(BSD) ? Server.setup(new KQueueEventLoopGroup())
                    : Server.setup();

            addShutdownHook(server);
            server.start();
            logger.info("server is up and running on {}:{}", server.getHost(), server.getPort());
        } catch (Exception e) {
            logger.error("application startup failed with message {}", e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Adds a shutdown hook for JVM to be closed gracefully.
     *
     * @param server Server instance created on application startup.
     */
    private static void addShutdownHook(Server server) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
            } catch (InterruptedException e) {
                logger.error("gracefully shutdown of application failed with message {}", e.getMessage());
            }
        }));
    }
}
