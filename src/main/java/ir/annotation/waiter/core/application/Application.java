package ir.annotation.waiter.core.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * An abstraction that is useful for being used on a class that is the entry point of an application running.
 *
 * @author Alireza Pourtaghi
 */
public abstract class Application implements ContextAware {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * Application specific properties. The default properties are empty.
     */
    private final Properties properties = new Properties();

    /**
     * Method that is called to start application.
     *
     * @param args Argument list passed to start method. Can be command line arguments and options.
     */
    protected void start(String[] args) {
        loadProperties(args);
        addShutdownHook();
        startComponents();
    }

    /**
     * Method that is called to stop application.
     */
    protected void stop() {
        stopComponents();
    }

    /**
     * Tries to load application properties from application.properties file located on resources folder of running app.
     * <p>
     * If the application started with --properties flag, the provided file path will be used to load application properties and overwrites the values loaded from application.properties located in resources folder.
     * </p>
     */
    protected void loadProperties(String[] args) {
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

    /**
     * Adds a shutdown hook for JVM to close application gracefully.
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    /**
     * Starts all components that are declared as fields on current application instance.
     * <p>
     * Before starting the application instance, adds jvm shutdown hook for application.
     * </p>
     */
    private void startComponents() {
        declaredComponents().forEach(component -> {
            component = (Component) component.setup(getProperties());
            getContext().addThenStartComponent(component);
        });
    }

    /**
     * Stops all components that are declared as fields on current application instance.
     */
    private void stopComponents() {
        declaredComponents().stream()
                .filter(Objects::nonNull)
                .forEach(component ->
                        getContext().stopComponentByIdentifier(component.getIdentifier())
                );
    }

    /**
     * Returns back all components defined as fields on this application instance.
     *
     * @return A list of all declared components.
     */
    private List<Component> declaredComponents() {
        var components = new ArrayList<Component>();

        for (Field field : getClass().getDeclaredFields()) {
            var type = field.getType();
            var superClass = type.getSuperclass();

            if (superClass != null)
                if (superClass.getName().equals(Component.class.getName())) {
                    try {
                        var component = (Component) type.getConstructor().newInstance();
                        components.add(component);
                    } catch (Exception e) {
                        logger.error("loading constructor for {} component failed with message {}", type.getName(), e.getMessage());
                    }
                }
        }

        return components;
    }

    protected final Properties getProperties() {
        return properties;
    }
}
