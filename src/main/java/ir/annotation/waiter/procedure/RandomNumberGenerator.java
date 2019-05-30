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
     * Constructor to create an instance of this procedure.
     */
    public RandomNumberGenerator() {
        super("generate_random_number");
    }

    @Override
    public CompletableFuture<Optional<Integer>> apply(ExecutorService executor, GenerateRandomNumberRequest generateRandomNumberRequest) {
        return CompletableFuture.supplyAsync(() -> Optional.of(generateRandomNumberRequest.getFrom() + generateRandomNumberRequest.getSecureRandom().nextInt(generateRandomNumberRequest.getTo() + 1)), executor);
    }

    /**
     * Generate random number request model.
     *
     * @author Alireza Pourtaghi
     */
    public static final class GenerateRandomNumberRequest {
        /**
         * Source of randomness.
         */
        private final SecureRandom secureRandom;

        /**
         * Generated random number will be greater than or equal to this field.
         */
        private final int from;

        /**
         * Generated random number will be less than or equal to this field.
         */
        private final int to;

        /**
         * Constructor to create an instance of this model.
         *
         * @param from Generated random number will be greater than or equal to this field.
         * @param to   Generated random number will be less than or equal to this field.
         */
        public GenerateRandomNumberRequest(int from, int to) {
            this.secureRandom = new SecureRandom();
            this.from = from;
            this.to = to;
        }

        /**
         * Constructor to create an instance of this model.
         *
         * @param secureRandom Source of randomness.
         * @param from         Generated random number will be greater than or equal to this field.
         * @param to           Generated random number will be less than or equal to this field.
         */
        public GenerateRandomNumberRequest(SecureRandom secureRandom, int from, int to) {
            this.secureRandom = secureRandom;
            this.from = from;
            this.to = to;
        }

        public SecureRandom getSecureRandom() {
            return secureRandom;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }
    }
}
