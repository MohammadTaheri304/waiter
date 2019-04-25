package ir.annotation.waiter.util;

import org.msgpack.value.Value;
import org.msgpack.value.impl.ImmutableArrayValueImpl;
import org.msgpack.value.impl.ImmutableMapValueImpl;
import org.msgpack.value.impl.ImmutableStringValueImpl;

/**
 * Utility class that is useful to generate message pack's {@link Value}s.
 *
 * @author Alireza Pourtaghi
 */
public class MessagePackUtil {

    /**
     * Generates a message pack's array format.
     *
     * @param values The array elements.
     * @return Newly created and ready to use {@link ImmutableArrayValueImpl}.
     */
    public static ImmutableArrayValueImpl array(Value... values) {
        return new ImmutableArrayValueImpl(values);
    }

    /**
     * Generates a message pack's map format.
     *
     * @param keyValues The map elements.
     * @return Newly created and ready to use {@link ImmutableMapValueImpl}.
     */
    public static ImmutableMapValueImpl map(Value... keyValues) {
        return new ImmutableMapValueImpl(keyValues);
    }

    /**
     * Generates a message pack's string format.
     *
     * @param value The string value.
     * @return Newly created and ready to use {@link ImmutableStringValueImpl}.
     */
    public static ImmutableStringValueImpl string(String value) {
        return new ImmutableStringValueImpl(value);
    }
}
