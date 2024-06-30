package zql.CallRope.point.log.exception;

public class ConfigException extends RuntimeException {
    public ConfigException(Exception e) {
        super(e);
    }

    public ConfigException(String message) {
        super(message);
    }
}
