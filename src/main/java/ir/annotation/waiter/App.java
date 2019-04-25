package ir.annotation.waiter;

import ir.annotation.waiter.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            var server = Server.setup();
            addShutdownHook(server);

            server.start();
            logger.info("server is up and running on {}:{}", server.getHost(), server.getPort());
        } catch (InterruptedException e) {
            logger.error("application startup failed with message {}", e.getMessage());
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
