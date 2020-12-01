package socialnetwork.domain.validators;

public class ValidationException extends RuntimeException {
    public ValidationException() {
    }

    /**
     *
     * @param message
     */
    public ValidationException(String message) {
        super(message);
    }
}
