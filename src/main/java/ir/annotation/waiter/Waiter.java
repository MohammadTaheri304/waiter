package ir.annotation.waiter;

import ir.annotation.waiter.core.application.Application;
import ir.annotation.waiter.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.nio.file.Paths;

/**
 * Waiter application main class.
 *
 * @author Alireza Pourtaghi
 */
public class Waiter extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Waiter.class);

    /**
     * Empty constructor.
     */
    public Waiter() {
    }

    @Override
    public void start(String[] args) {
        loadProperties(args);
        context.addComponentThenStart(new Server().setup(getProperties()));
    }

    @Override
    public void stop() {
        context.stopComponentByIdentifier("netty_server");
    }

    /**
     * Tries to load application properties from application.properties file located on resources folder of running app.
     * <p>
     * If the application started with -P or --properties flag the provided file path will be used to load application properties and overwrites the values loaded from application.properties.
     * </p>
     */
    private void loadProperties(String[] args) {
        try {
            try (var resourceReader = ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties")) {
                if (resourceReader != null) {
                    getProperties().load(resourceReader);
                }
            }

            if (args.length == 2 && args[0].equals("--properties")) {
                logger.info("loading application properties from {} ...", args[1]);
                try (var fileReader = new FileInputStream(Paths.get(args[1]).toFile())) {
                    getProperties().load(fileReader);
                }
            }
        } catch (Exception e) {
            logger.error("loading application properties failed with message {}", e.getMessage());
            System.exit(-1);
        }
    }
}