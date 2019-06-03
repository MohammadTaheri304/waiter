package ir.annotation.waiter;

import ir.annotation.waiter.core.application.Application;
import ir.annotation.waiter.server.Server;

/**
 * Main application class that includes main method.
 *
 * @author Alireza Pourtaghi
 */
public final class Main extends Application {
    /**
     * {@link Server} component.
     */
    private Server server;

    /**
     * Main method that is invoked by JVM to start application.
     *
     * @param args Arguments passed to the application on startup, including options and flags.
     */
    public static void main(String[] args) {
        new Main().start(args);
    }
}
