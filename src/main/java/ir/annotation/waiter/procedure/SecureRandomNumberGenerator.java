package ir.annotation.waiter.procedure;

import ir.annotation.waiter.core.procedure.AsynchronousProcedure;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * A random number generator that generates numbers from 111111 to 999999 inclusive.
 *
 * @author Alireza Pourtaghi
 */
public class SecureRandomNumberGenerator extends AsynchronousProcedure<Void, Integer> {
    /**
     * Java's secure random instance.
     */
    private final SecureRandom secureRandom;

    /**
     * Constructor to create an instance of this procedure.
     */
    public SecureRandomNumberGenerator() {
        super("generate_secure_random_number");
        this.secureRandom = new SecureRandom();
    }

    @Override
    public CompletableFuture<Optional<Integer>> apply(ExecutorService executor, Void aVoid) {
        return CompletableFuture.supplyAsync(() -> Optional.of(111111 + secureRandom.nextInt(888888 + 1)), executor);
    }
}
