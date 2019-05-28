package ir.annotation.waiter.procedure;

import ir.annotation.waiter.core.procedure.AsynchronousProcedure;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

/**
 * Key exchange procedure implementation.
 *
 * @author Alireza Pourtaghi
 */
public class KeyExchanger extends AsynchronousProcedure<KeyExchanger.KeyExchangeRequest, KeyExchanger.KeyExchangeResponse> {

    /**
     * Constructor to create an instance of this procedure.
     */
    public KeyExchanger() {
        super("exchange_key");
    }

    @Override
    public CompletableFuture<Optional<KeyExchangeResponse>> apply(ExecutorService executor, KeyExchangeRequest keyExchangeRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var keyPairGenerator = KeyPairGenerator.getInstance(keyExchangeRequest.getAlgorithm().name());
                keyPairGenerator.initialize(keyExchangeRequest.getKeySize().getSize(), keyExchangeRequest.getSecureRandom());

                var keyPair = keyPairGenerator.generateKeyPair();

                var keyAgreement = KeyAgreement.getInstance(keyExchangeRequest.getAlgorithm().name());
                keyAgreement.init(keyPair.getPrivate(), keyExchangeRequest.getSecureRandom());
                keyAgreement.doPhase(keyExchangeRequest.getOtherPartyKey(), true);
                var secretKey = keyAgreement.generateSecret();

                return Optional.of(new KeyExchangeResponse(keyPair.getPrivate(), keyPair.getPublic(), secretKey));
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    /**
     * Key exchange request model.
     *
     * @author Alireza Pourtaghi
     */
    public static final class KeyExchangeRequest {
        /**
         * Source of randomness.
         */
        private final SecureRandom secureRandom;

        /**
         * The algorithm to use for key exchange.
         */
        private final Algorithm algorithm;

        /**
         * Key size of generated key pair.
         */
        private final KeySize keySize;

        /**
         * The key of other party involved in this key agreement.
         */
        private final Key otherPartyKey;

        /**
         * Constructor to create an instance of this model.
         *
         * @param secureRandom  Source of randomness.
         * @param algorithm     The algorithm to use for key exchange.
         * @param keySize       Key size of generated key pairs.
         * @param otherPartyKey The key of other party involved in this key agreement.
         */
        public KeyExchangeRequest(SecureRandom secureRandom, Algorithm algorithm, KeySize keySize, Key otherPartyKey) {
            this.secureRandom = secureRandom;
            this.algorithm = algorithm;
            this.keySize = keySize;
            this.otherPartyKey = otherPartyKey;
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

        public Key getOtherPartyKey() {
            return otherPartyKey;
        }

        /**
         * Available key exchange algorithms.
         *
         * @author Alireza Pourtaghi
         */
        public enum Algorithm {
            DiffieHellman
        }

        /**
         * Available key size for generated key pair.
         *
         * @author Alireza Pourtaghi
         */
        public enum KeySize {
            _1024(1024),
            _2048(2048),
            _4096(4096);

            private final int size;

            KeySize(int size) {
                this.size = size;
            }

            public int getSize() {
                return size;
            }
        }
    }

    /**
     * Key exchange response model.
     *
     * @author Alireza Pourtaghi
     */
    public static final class KeyExchangeResponse {
        /**
         * Generated private key of this party.
         */
        private final PrivateKey privateKey;

        /**
         * Generated public key of this party.
         */
        private final PublicKey publicKey;

        /**
         * Generated shared secret key.
         */
        private final byte[] secretKey;


        /**
         * Constructor to create an instance of this model.
         *
         * @param privateKey Generated private key of this party.
         * @param publicKey  Generated public key of this party.
         * @param secretKey  Generated shared secret key.
         */
        public KeyExchangeResponse(PrivateKey privateKey, PublicKey publicKey, byte[] secretKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
            this.secretKey = secretKey;
        }

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

        public byte[] getSecretKey() {
            return secretKey;
        }
    }
}
