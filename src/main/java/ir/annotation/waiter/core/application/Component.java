package ir.annotation.waiter.core.application;

import ir.annotation.waiter.core.common.Identity;

import java.util.Properties;

/**
 * An application component can be any type of component that needs to be sync with application life cycle.
 * <p>
 * Components can override start method so start will be called on application startup; and also can override stop method so stop will be called on application shutdown.
 * </p>
 *
 * @param <T> The type of component.
 * @param <R> The return type of start method. Sometimes useful.
 */
public abstract class Component<T, R> extends Identity implements ContextAware {

    /**
     * Constructor to provide an identifier for creating component.
     *
     * @param identifier The identifier of component.
     */
    public Component(String identifier) {
        super(identifier);
    }

    /**
     * Setups the component by using provided properties.
     *
     * @param properties The application properties loaded for application startup.
     */
    public abstract T setup(Properties properties);

    /**
     * Start will be called on application startup to start component running.
     *
     * @throws Exception If component start call failed.
     */
    public abstract R start() throws Exception;

    /**
     * Stop will be called on application shutdown ro stop component from running.
     *
     * @throws Exception If component start call failed.
     */
    public abstract void stop() throws Exception;
}
