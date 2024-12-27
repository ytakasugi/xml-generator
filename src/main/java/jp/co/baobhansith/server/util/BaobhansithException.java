package jp.co.baobhansith.server.util;

public class BaobhansithException extends Exception {
    public BaobhansithException(String message) {
        super(message);
    }

    public BaobhansithException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaobhansithException(String messageTemplate, Object... args) {
        super(String.format(messageTemplate, args));
    }

    public BaobhansithException(Throwable cause) {
        super(cause);
    }
}