package Exceptions;

public class TokenException extends Exception {
    public TokenException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
