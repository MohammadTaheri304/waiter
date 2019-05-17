package ir.annotation.waiter.core.application;

/**
 * Interface that is used on classes which must be aware of the same context.
 *
 * @author Alireza Pourtaghi
 */
interface ContextAware {

    /**
     * Context that is shared between all {@link ContextAware} instances. The default context is an empty one.
     */
    Context context = new Context();

    default Context getContext() {
        return context;
    }
}
