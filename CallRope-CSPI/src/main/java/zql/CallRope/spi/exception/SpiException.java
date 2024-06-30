package zql.CallRope.spi.exception;

public class SpiException extends RuntimeException{

    public SpiException(String message) {
        // TODO log
        super(message);
    }

    public SpiException(String message, Throwable cause) {
        // TODO log
        super(message, cause);
    }

    public SpiException(Throwable cause) {
        // TODO log
        super(cause);
    }
}
