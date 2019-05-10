package ir.annotation.waiter.core;

import ir.annotation.waiter.core.common.AbstractProcedure;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * An implementation of {@link AbstractProcedure} that acts as a normal synchronous function.
 * <p>
 * This procedure also implements {@link Function} to provide same functionality.
 * </p>
 *
 * @param <T> The type of the input to the procedure.
 * @param <R> The type of the result of the procedure.
 * @author Alireza Pourtaghi
 */
public class Procedure<T, R> extends AbstractProcedure<T, R> implements Function<T, Optional<R>> {
    /**
     * Constructor to create an instance of procedure.
     *
     * @param identifier The identifier of procedure.
     * @throws NullPointerException If provided identifier is {@code null}.
     */
    public Procedure(String identifier) {
        super(identifier);
    }

    @Override
    public Optional<R> apply(T t) {
        return Optional.empty();
    }

    @Override
    public final CompletableFuture<Optional<R>> apply(ExecutorService executor, T t) {
        return CompletableFuture.completedFuture(Optional.empty());
    }
}
