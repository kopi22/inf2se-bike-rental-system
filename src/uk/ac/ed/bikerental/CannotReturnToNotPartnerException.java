package uk.ac.ed.bikerental;

public class CannotReturnToNotPartnerException extends RuntimeException {

    public CannotReturnToNotPartnerException() {
    }

    public CannotReturnToNotPartnerException(String message) {
        super(message);
    }

    public CannotReturnToNotPartnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotReturnToNotPartnerException(Throwable cause) {
        super(cause);
    }

    public CannotReturnToNotPartnerException(String message, Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
