package ir.annotation.waiter.core;

import ir.annotation.waiter.core.common.Identity;

import java.util.function.Function;

/**
 * A procedure is simply a {@link Function} that takes one argument as parameter and returns a response.
 *
 * @author Alireza Pourtaghi
 */
public class Procedure extends Identity implements Function<Object, Object> {
    /**
     * Constructor to create an instance of this procedure.
     *
     * @param identifier The unique identifier of this procedure.
     * @throws NullPointerException If provided identifier is {@code null}.
     */
    public Procedure(String identifier) {
        super(identifier);
    }

    /**
     * The default implementation of this procedure. Only returns a new {@link Object}.
     *
     * @param o Provided argument.
     * @return The result value of procedure invocation.
     */
    @Override
    public Object apply(Object o) {
        return new Object();
    }

    @Override
    public final boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
