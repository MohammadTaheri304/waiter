package ir.annotation.waiter.server;

/**
 * An error that should be used on all layers when error occurs, including services.
 *
 * @author Alireza Pourtaghi
 */
public class Error extends RuntimeException {
    /**
     * Error code.
     */
    private final String code;

    /**
     * Constructor to create an error instance.
     *
     * @param code    Error code.
     * @param message Appropriate message related to error code.
     */
    private Error(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * The error reason list.
     *
     * @author Alireza Pourtaghi
     */
    public enum Reason {
        INTERNAL_SERVER_ERROR(new Error("internal.server.error", "Internal server error.")),
        INVALID_MESSAGE_FORMAT(new Error("invalid.message.format", "Message format is not valid. See specification.")),
        RESOURCE_NOT_FOUND(new Error("resource.not.found", "The requested resource not found."));

        /**
         * The error exception.
         */
        private final Error error;

        /**
         * Enum constructor to create an instance of available constants.
         *
         * @param error The error exception.
         */
        Reason(Error error) {
            this.error = error;
        }

        public Error getError() {
            return error;
        }
    }
}
