package ir.annotation.waiter.procedure;

import ir.annotation.waiter.core.procedure.AsynchronousProcedure;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Public-Private key pair generator implementation.
 *
 * @author Alireza Pourtaghi
 */
public class PublicPrivateKeyPairGenerator extends AsynchronousProcedure<PublicPrivateKeyPairGenerator.GenerateKeyPairRequest, KeyPair> {

    /**
     * Constructor to create an instance of this procedure.
     */
    public PublicPrivateKeyPairGenerator() {
        super("generate_public_private_key_pair");
    }

    @Override
    public CompletableFuture<Optional<KeyPair>> apply(ExecutorService executor, GenerateKeyPairRequest generateKeyPairRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var keyPairGenerator = KeyPairGenerator.getInstance(generateKeyPairRequest.getAlgorithm().getName());
                keyPairGenerator.initialize(generateKeyPairRequest.getKeySize().getSize(), generateKeyPairRequest.getSecureRandom());

                return Optional.of(keyPairGenerator.generateKeyPair());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    /**
     * Generate public-private key pair request model.
     *
     * @author Alireza Pourtaghi
     */
    public static final class GenerateKeyPairRequest {
        /**
         * Source of randomness.
         */
        private final SecureRandom secureRandom;

        /**
         * The algorithm to use for public-private key pair generator.
         */
        private final Algorithm algorithm;

        /**
         * Key size of generated public-private key pair.
         */
        private final KeySize keySize;

        /**
         * Constructor to create an instance of this model.
         *
         * @param algorithm The algorithm to use for public-private key pair generator.
         * @param keySize   Key size of generated public-private key pair.
         */
        public GenerateKeyPairRequest(Algorithm algorithm, KeySize keySize) {
            this.secureRandom = new SecureRandom();
            this.algorithm = algorithm;
            this.keySize = keySize;
        }

        /**
         * Constructor to create an instance of this model.
         *
         * @param secureRandom Source of randomness.
         * @param algorithm    The algorithm to use for public-private key pair generator.
         * @param keySize      Key size of generated public-private key pair.
         */
        public GenerateKeyPairRequest(SecureRandom secureRandom, Algorithm algorithm, KeySize keySize) {
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
         * Available public-private key pair generator algorithms.
         *
         * @author Alireza Pourtaghi
         */
        public enum Algorithm {
            DiffieHellman("DiffieHellman"),
            DSA("DSA"),
            RSA("RSA"),
            RSASSA_PSS("RSASSA-PSS"),
            EC("EC"),
            XDH("XDH"),
            X25519("X25519"),
            X448("X448");

            /**
             * The name of algorithm to use.
             */
            private final String name;

            /**
             * Enum constructor to create an instance of available constants.
             *
             * @param name The name of algorithm to use.
             */
            Algorithm(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        /**
         * Available key size for generator algorithm.
         *
         * @author Alireza Pourtaghi
         */
        public enum KeySize {
            _1024(1024),
            _2048(2048),
            _4096(4096);

            /**
             * The key size to use.
             */
            private final int size;

            /**
             * Enum constructor to create an instance of available constants.
             *
             * @param size The key size to use.
             */
            KeySize(int size) {
                this.size = size;
            }

            public int getSize() {
                return size;
            }
        }
    }
}
