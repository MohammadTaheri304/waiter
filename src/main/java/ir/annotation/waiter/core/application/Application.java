package ir.annotation.waiter.core.application;

import java.util.Properties;

/**
 * An abstraction that is useful for being used on a class that is the entry point of an application running.
 *
 * @author Alireza Pourtaghi
 */
public abstract class Application implements ContextAware {

    /**
     * Application specific properties. The default properties are empty.
     */
    private final Properties properties = new Properties();

    /**
     * Method that is called to start application.
     *
     * @param args Argument list passed to start method. Can be command line arguments and options.
     */
    protected abstract void start(String[] args);

    /**
     * Method that is called to stop application.
     */
    protected abstract void stop();

    /**
     * Adds provided component to the context then calls start on it.
     *
     * @param component The {@link Component} that should be add to the context.
     * @throws NullPointerException If provided component is {@code null}.
     */
    protected final void addThenStartComponent(Component component) {
        getContext().addThenStartComponent(component);
    }

    /**
     * Stops provided component by calling its stop method.
     *
     * @param identifier The component's identifier that should be find on context.
     * @throws NullPointerException If provided component is {@code null}.
     */
    protected final void stopComponentByIdentifier(String identifier) {
        getContext().stopComponentByIdentifier(identifier);
    }

    protected Properties getProperties() {
        return properties;
    }
}
