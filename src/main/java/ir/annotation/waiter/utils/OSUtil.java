package ir.annotation.waiter.utils;

import java.util.Locale;

/**
 * Utility class to detect operating system name.
 * <p>
 * Implementation from https://github.com/trustin/os-maven-plugin .
 * </p>
 *
 * @author Alireza Pourtaghi
 */
public class OSUtil {

    /**
     * Tries to detect operating system name that this application is running on.
     *
     * @return An operating system identifier. See {@link OS}.
     */
    public static OS detectOS() {
        var value = normalize(System.getProperty("os.name"));

        if (value.startsWith("linux")) {
            return OS.LINUX;
        } else if (value.startsWith("macosx") || value.startsWith("osx")) {
            return OS.OSX;
        } else if (value.startsWith("freebsd") || value.startsWith("openbsd") || value.startsWith("netbsd")) {
            return OS.BSD;
        }

        return OS.UNKNOWN;
    }

    /**
     * Normalizes string value to make os name detection simpler.
     *
     * @param value The value that should be normalized.
     * @return The normalized value.
     */
    private static String normalize(String value) {
        if (value == null)
            return "";

        return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
    }

    /**
     * Operating system identifiers.
     *
     * @author Alireza Pourtaghi
     */
    public enum OS {
        LINUX,
        OSX,
        BSD,
        UNKNOWN
    }
}
