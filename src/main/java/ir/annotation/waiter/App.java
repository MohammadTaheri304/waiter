package ir.annotation.waiter;

import ir.annotation.waiter.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

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
            var properties = loadProperties(args);
            var server = Server.setup(properties);

            addShutdownHook(server);
            server.start();
            logger.info("server is up and running on {}:{}", server.getHost(), server.getPort());
        } catch (Exception e) {
            logger.error("application startup failed with message {}", e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Tries to load application properties from application.properties file located on resources folder of running app.
     * <p>
     * If the application started with -P or --properties flag the provided file path will be used to load application properties and overwrites the values loaded from application.properties.
     * </p>
     *
     * @return Created and loaded {@link Properties} instance with provided values in file.
     * @throws IOException If exception occurred during file processing.
     */
    private static Properties loadProperties(String[] args) throws IOException {
        var properties = new Properties();

        try (var resourceReader = ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties")) {
            if (resourceReader != null) {
                properties.load(resourceReader);
            }
        }

        if (args.length == 2 && (args[0].equals("-P") || args[0].equals("--properties"))) {
            logger.info("loading application properties from {} ...", args[1]);
            try (var fileReader = new FileInputStream(Paths.get(args[1]).toFile())) {
                properties.load(fileReader);
            }
        }

        return properties;
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
