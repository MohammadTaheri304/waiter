package ir.annotation.waiter.core.application;

import ir.annotation.waiter.core.application.annotation.StartOrder;
import ir.annotation.waiter.core.application.annotation.StopOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        logger.info("starting application components ...");
        loadProperties(args);
        addShutdownHook();
        startComponents();
        logger.info("application components started successfully");
    }

    /**
     * Method that is called to stop application.
     */
    protected void stop() {
        logger.info("stopping application components ...");
        stopComponents();
        logger.info("application components stopped successfully");
    }

    /**
     * Tries to load application properties from application.properties file located on resources folder of running app.
     * <p>
     * If the application started with --properties flag, the provided file path will be used to load application properties and overwrites the values loaded from application.properties located in resources folder.
     * </p>
     *
     * @param args The arguments passed on application startup including options and flags.
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
     * Starts all components that are declared as fields on current application instance.
     * <p>
     * Before starting the application instance, adds jvm shutdown hook for application.
     * </p>
     */
    private void startComponents() {
        instantiateDeclaredComponents().forEach(component -> {
            component = (Component) component.setup(getProperties());
            getContext().addThenStartComponent(component);
        });
    }

    /**
     * Stops all components that are declared as fields on current application instance.
     */
    private void stopComponents() {
        loadDeclaredComponents().forEach(component ->
                getContext().stopComponentByIdentifier(component.getIdentifier())
        );
    }

    /**
     * Returns back all components defined as fields on this application instance.
     *
     * @return A list of all declared components.
     */
    private List<Component> instantiateDeclaredComponents() {
        var components = new ArrayList<Component>();
        var fields = getClass().getDeclaredFields();

        for (Field field : determineStartingOrder(fields)) {
            var type = field.getType();
            var superClass = type.getSuperclass();

            if (superClass != null)
                if (superClass.getName().equals(Component.class.getName())) {
                    try {
                        if (Modifier.isPrivate(field.getModifiers()))
                            field.setAccessible(true);

                        var component = (Component) type.getConstructor().newInstance();
                        field.set(this, component);
                        components.add(component);
                    } catch (Exception e) {
                        logger.error("instantiating constructor for {} component failed with message {}", type.getName(), e.getMessage());
                    }
                }
        }

        return components;
    }

    /**
     * Returns back all not null components defined as fields on this application instance.
     *
     * @return A list of all declared components.
     */
    private List<Component> loadDeclaredComponents() {
        var components = new ArrayList<Component>();
        var fields = getClass().getDeclaredFields();

        for (Field field : determineStoppingOrder(fields)) {
            var type = field.getType();
            var superClass = type.getSuperclass();

            if (superClass != null)
                if (superClass.getName().equals(Component.class.getName())) {
                    try {
                        if (Modifier.isPrivate(field.getModifiers()))
                            field.setAccessible(true);

                        var component = field.get(this);
                        if (component != null)
                            components.add((Component) component);
                    } catch (Exception e) {
                        logger.error("loading {} component failed with message {}", type.getName(), e.getMessage());
                    }
                }
        }

        return components;
    }

    /**
     * Sorts passed field array based on {@link StartOrder} annotation.
     *
     * @param fields The unsorted array that must be sorted.
     * @return Sorted array, same reference.
     */
    private Field[] determineStartingOrder(Field[] fields) {
        Arrays.sort(fields, (f1, f2) -> {
            var o1 = f1.getAnnotation(StartOrder.class);
            var o2 = f2.getAnnotation(StartOrder.class);

            if (o1 != null && o2 != null) {
                return o1.value() - o2.value();
            } else if (o1 != null) {
                return -1;
            } else if (o2 != null) {
                return 1;
            } else {
                return f1.getName().compareTo(f2.getName());
            }
        });

        return fields;
    }

    /**
     * Sorts passed field array based on {@link StopOrder} annotation.
     *
     * @param fields The unsorted array that must be sorted.
     * @return Sorted array, same reference.
     */
    private Field[] determineStoppingOrder(Field[] fields) {
        Arrays.sort(fields, (f1, f2) -> {
            var o1 = f1.getAnnotation(StopOrder.class);
            var o2 = f2.getAnnotation(StopOrder.class);

            if (o1 != null && o2 != null) {
                return o1.value() - o2.value();
            } else if (o1 != null) {
                return -1;
            } else if (o2 != null) {
                return 1;
            } else {
                return f1.getName().compareTo(f2.getName());
            }
        });

        return fields;
    }

    /**
     * Adds a shutdown hook for JVM to close application gracefully.
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    protected final Properties getProperties() {
        return properties;
    }
}
