package socialnetwork.domain.validators;

public class NonExistentObjectException extends RuntimeException {
    public NonExistentObjectException() {
    }

    public NonExistentObjectException(String message) {
        super(message);
    }

    public NonExistentObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistentObjectException(Throwable cause) {
        super(cause);
    }

    public NonExistentObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

