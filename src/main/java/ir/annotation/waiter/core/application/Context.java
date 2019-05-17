package ir.annotation.waiter.core.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * An application context implementation that is used internally.
 *
 * @author Alireza Pourtaghi
 */
final class Context {
    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    /**
     * {@link Component}s that is injected to context.
     */
    private final ConcurrentHashMap.KeySetView<Component, Boolean> components = ConcurrentHashMap.newKeySet();

    /**
     * Empty package level constructor.
     */
    Context() {
    }

    /**
     * Adds provided component to the context then calls start on it.
     *
     * @param component The {@link Component} that should be add to the context.
     * @throws NullPointerException If provided component is {@code null}.
     */
    void addThenStartComponent(Component component) {
        requireNonNull(component);

        try {
            components.add(component);
            component.start();
        } catch (Exception e) {
            logger.error("start call failed on {} with message {}", component.getIdentifier(), e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Stops provided component by calling its stop method.
     *
     * @param identifier The component's identifier that should be find on context.
     * @throws NullPointerException If provided component is {@code null}.
     */
    void stopComponentByIdentifier(String identifier) {
        requireNonNull(identifier);

        getComponentByIdentifier(identifier).ifPresentOrElse(
                component -> {
                    try {
                        component.stop();
                    } catch (Exception e) {
                        logger.error("stop call failed on {} with message {}", component.getIdentifier(), e.getMessage());
                    }
                },
                () -> logger.warn("no component found with {} as identifier!", identifier)
        );
    }

    /**
     * Returns back a component from context.
     *
     * @param identifier The component's identifier that should be find on context.
     * @return Optional {@link Component} that has same identifier as provided one.
     * @throws NullPointerException If provided identifier is {@code null}.
     */
    public Optional<Component> getComponentByIdentifier(String identifier) {
        requireNonNull(identifier);

        return components.stream()
                .filter(component -> component.getIdentifier().equals(identifier))
                .findFirst();
    }
}
