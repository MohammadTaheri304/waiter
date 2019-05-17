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

    public Properties getProperties() {
        return properties;
    }
}
