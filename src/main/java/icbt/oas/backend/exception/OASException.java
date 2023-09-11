package icbt.oas.backend.exception;
public class OASException extends RuntimeException {
    public OASException(String message, Throwable cause) {
        super(message, cause);
    }
}
