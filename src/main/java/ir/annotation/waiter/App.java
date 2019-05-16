package ir.annotation.waiter;

import ir.annotation.waiter.core.application.Waiter;

/**
 * Main application class that includes main method.
 *
 * @author Alireza Pourtaghi
 */
public final class App {

    /**
     * Main method that is invoked by JVM to start application.
     *
     * @param args Arguments passed to the application on startup, including options and flags.
     */
    public static void main(String[] args) {
        var waiter = new Waiter();
        addShutdownHook(waiter);

        waiter.start(args);
    }

    /**
     * Adds a shutdown hook for JVM to be closed gracefully.
     *
     * @param waiter Waiter instance created on application startup.
     */
    private static void addShutdownHook(Waiter waiter) {
        Runtime.getRuntime().addShutdownHook(new Thread(waiter::stop));
    }
}
