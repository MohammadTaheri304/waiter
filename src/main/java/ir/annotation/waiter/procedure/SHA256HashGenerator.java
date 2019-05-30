package ir.annotation.waiter.procedure;

import ir.annotation.waiter.core.procedure.AsynchronousProcedure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * A SHA-256 hashing procedure.
 *
 * @author Alireza Pourtaghi
 */
public class SHA256HashGenerator extends AsynchronousProcedure<byte[], byte[]> {
    /**
     * Message hashing algorithm.
     */
    private final MessageDigest messageDigest;

    /**
     * Constructor to create an an instance of this procedure.
     *
     * @throws NoSuchAlgorithmException If desired algorithm name does not exists.
     */
    public SHA256HashGenerator() throws NoSuchAlgorithmException {
        super("generate_SHA256_hash");
        this.messageDigest = MessageDigest.getInstance("SHA-256");
    }

    @Override
    public CompletableFuture<Optional<byte[]>> apply(ExecutorService executor, byte[] bytes) {
        return CompletableFuture.supplyAsync(() -> Optional.of(messageDigest.digest(bytes)), executor);
    }
}
