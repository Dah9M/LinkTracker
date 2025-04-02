package exception;

import lombok.Getter;

public class UrlValidationException extends RuntimeException {
    @Getter
    private final UrlValidationErrorType errorType;

    public UrlValidationException(UrlValidationErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }
}