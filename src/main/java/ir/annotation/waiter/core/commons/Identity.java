package ir.annotation.waiter.core.commons;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * An identity abstract class that can be used on classes where instances must be identified by an identifier.
 *
 * @author Alireza Pourtaghi
 */
public abstract class Identity {
    /**
     * The identifier of an instance of this abstract class.
     */
    private final String identifier;

    /**
     * Constructor to build an instance of identity abstract class.
     *
     * @param identifier The identifier of an instance of this abstract class.
     * @throws NullPointerException If provided identifier is {@code null}.
     */
    public Identity(String identifier) {
        requireNonNull(identifier);

        this.identifier = identifier;
    }

    public final String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equals(identifier, identity.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
