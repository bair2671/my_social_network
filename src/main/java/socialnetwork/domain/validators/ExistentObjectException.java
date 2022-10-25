package socialnetwork.domain.validators;

public class ExistentObjectException extends RuntimeException {
    public ExistentObjectException() {
    }

    public ExistentObjectException(String message) {
        super(message);
    }

    public ExistentObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistentObjectException(Throwable cause) {
        super(cause);
    }

    public ExistentObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

