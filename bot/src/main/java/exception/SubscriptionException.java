package exception;

import lombok.Getter;

public class SubscriptionException extends RuntimeException {
    @Getter
    private final SubscriptionErrorType errorType;

    public SubscriptionException(SubscriptionErrorType error, String message) {
        super(message);
        this.errorType = error;
    }
}
