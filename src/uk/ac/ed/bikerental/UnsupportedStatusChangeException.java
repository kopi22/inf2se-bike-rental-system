package uk.ac.ed.bikerental;

public class UnsupportedStatusChangeException extends UnsupportedOperationException {

    public UnsupportedStatusChangeException() {
    }

    public UnsupportedStatusChangeException(String message) {
        super(message);
    }

    public UnsupportedStatusChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedStatusChangeException(Throwable cause) {
        super(cause);
    }
}
