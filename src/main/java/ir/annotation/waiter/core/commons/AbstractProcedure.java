package ir.annotation.waiter.core.commons;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * An abstract procedure definition.
 *
 * @param <T> The type of the input to the procedure.
 * @param <R> The type of the result of the procedure.
 * @author Alireza Pourtaghi
 */
public abstract class AbstractProcedure<T, R> extends Identity {
    /**
     * Constructor to create an instance of procedure.
     *
     * @param identifier The identifier of procedure.
     * @throws NullPointerException If provided identifier is {@code null}.
     */
    public AbstractProcedure(String identifier) {
        super(identifier);
    }

    /**
     * Applies the provided t argument to an instance of this abstract class and returns optional r as response.
     *
     * @param t The argument that must be provided on procedure call.
     * @return Optional value that holds the response.
     */
    public abstract Optional<R> apply(T t);

    /**
     * Applies the provided t argument to an instance of this abstract class and returns optional r as response.
     * <p>
     * The procedure will be run on provided executor service.
     * </p>
     *
     * @param executor The provided executor service that this procedure must be called on.
     * @param t        The argument that must be provided on procedure call.
     * @return Asynchronous optional value that holds the response. See {@link CompletableFuture} for more info.
     */
    public abstract CompletableFuture<Optional<R>> apply(ExecutorService executor, T t);

    @Override
    public final boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
