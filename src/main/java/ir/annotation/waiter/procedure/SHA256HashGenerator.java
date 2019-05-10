package ir.annotation.waiter.procedure;

import ir.annotation.waiter.core.AsynchronousProcedure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * A SHA-256 hashing procedure.
 *
 * @author Alireza Pourtaghi
 */
public class SHA256HashGenerator extends AsynchronousProcedure<byte[], String> {
    /**
     * Message hashing algorithm.
     */
    private final MessageDigest messageDigest;

    /**
     * Base64 encoder.
     */
    private final Base64.Encoder encoder;

    /**
     * Constructor to create an an instance of this procedure.
     *
     * @throws NoSuchAlgorithmException If desired algorithm name does not exists.
     */
    public SHA256HashGenerator() throws NoSuchAlgorithmException {
        super("generate_SHA256_hash");
        this.messageDigest = MessageDigest.getInstance("SHA-256");
        this.encoder = Base64.getEncoder();
    }

    @Override
    public CompletableFuture<Optional<String>> apply(ExecutorService executor, byte[] bytes) {
        return CompletableFuture.supplyAsync(() -> Optional.of(encoder.encodeToString(messageDigest.digest(bytes))), executor);
    }
}
