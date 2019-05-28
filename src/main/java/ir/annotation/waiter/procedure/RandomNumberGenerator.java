package ir.annotation.waiter.procedure;

import ir.annotation.waiter.core.procedure.AsynchronousProcedure;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * A random number generator that generates numbers in different ranges.
 *
 * @author Alireza Pourtaghi
 */
public class RandomNumberGenerator extends AsynchronousProcedure<RandomNumberGenerator.GenerateRandomNumberRequest, Integer> {
    /**
     * Java's secure random instance.
     */
    private final SecureRandom secureRandom;

    /**
     * Constructor to create an instance of this procedure.
     */
    public RandomNumberGenerator() {
        super("generate_random_number");
        this.secureRandom = new SecureRandom();
    }

    @Override
    public CompletableFuture<Optional<Integer>> apply(ExecutorService executor, GenerateRandomNumberRequest generateRandomNumberRequest) {
        return CompletableFuture.supplyAsync(() -> Optional.of(generateRandomNumberRequest.getFromInclusive() + secureRandom.nextInt(generateRandomNumberRequest.getToInclusive() + 1)), executor);
    }

    /**
     * Generate random number request model.
     *
     * @author Alireza Pourtaghi
     */
    public static final class GenerateRandomNumberRequest {
        /**
         * Generated random number will be greater than or equal to this field.
         */
        private final int fromInclusive;

        /**
         * Generated random number will be less than or equal to this field.
         */
        private final int toInclusive;

        /**
         * Constructor to create an instance of this model.
         *
         * @param fromInclusive Generated random number will be greater than or equal to this field.
         * @param toInclusive   Generated random number will be less than or equal to this field.
         */
        public GenerateRandomNumberRequest(int fromInclusive, int toInclusive) {
            this.fromInclusive = fromInclusive;
            this.toInclusive = toInclusive;
        }

        public int getFromInclusive() {
            return fromInclusive;
        }

        public int getToInclusive() {
            return toInclusive;
        }
    }
}
