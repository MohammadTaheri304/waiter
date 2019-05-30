package ir.annotation.waiter.procedure;

import ir.annotation.waiter.core.procedure.AsynchronousProcedure;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Secret key generator implementation.
 *
 * @author Alireza Pourtaghi
 */
public class SecretKeyGenerator extends AsynchronousProcedure<SecretKeyGenerator.GenerateSecretKeyRequest, SecretKey> {

    /**
     * Constructor to create an instance of this procedure.
     */
    public SecretKeyGenerator() {
        super("generate_secret_key");
    }

    @Override
    public CompletableFuture<Optional<SecretKey>> apply(ExecutorService executor, GenerateSecretKeyRequest generateSecretKeyRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var keyGenerator = KeyGenerator.getInstance(generateSecretKeyRequest.getAlgorithm().name());
                keyGenerator.init(generateSecretKeyRequest.getKeySize().getSize(), generateSecretKeyRequest.getSecureRandom());

                return Optional.of(keyGenerator.generateKey());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    /**
     * Generate secret key request model.
     *
     * @author Alireza Pourtaghi
     */
    public static final class GenerateSecretKeyRequest {
        /**
         * Source of randomness.
         */
        private final SecureRandom secureRandom;

        /**
         * The algorithm to use for secret key generator.
         */
        private final Algorithm algorithm;

        /**
         * Key size of generated secret key.
         */
        private final KeySize keySize;

        /**
         * Constructor to create an instance of this model.
         *
         * @param algorithm The algorithm to use for secret key generator.
         * @param keySize   Key size of generated secret key.
         */
        public GenerateSecretKeyRequest(Algorithm algorithm, KeySize keySize) {
            this.secureRandom = new SecureRandom();
            this.algorithm = algorithm;
            this.keySize = keySize;
        }

        /**
         * Constructor to create an instance of this model.
         *
         * @param secureRandom Source of randomness.
         * @param algorithm    The algorithm to use for secret key generator.
         * @param keySize      Key size of generated secret key.
         */
        public GenerateSecretKeyRequest(SecureRandom secureRandom, Algorithm algorithm, KeySize keySize) {
            this.secureRandom = secureRandom;
            this.algorithm = algorithm;
            this.keySize = keySize;
        }

        public SecureRandom getSecureRandom() {
            return secureRandom;
        }

        public Algorithm getAlgorithm() {
            return algorithm;
        }

        public KeySize getKeySize() {
            return keySize;
        }

        /**
         * Available secret key generator algorithms.
         *
         * @author Alireza Pourtaghi
         */
        public enum Algorithm {
            AES,
            ARCFOUR,
            Blowfish,
            ChaCha20,
            DES,
            DESede,
            HmacMD5,
            HmacSHA1,
            HmacSHA224,
            HmacSHA256,
            HmacSHA384,
            HmacSHA512,
            RC2
        }

        /**
         * Available secret key size for generator algorithm.
         *
         * @author Alireza Pourtaghi
         */
        public enum KeySize {
            _56(56),
            _128(128),
            _168(168),
            _192(192),
            _256(256);

            private final int size;

            KeySize(int size) {
                this.size = size;
            }

            public int getSize() {
                return size;
            }
        }
    }
}
