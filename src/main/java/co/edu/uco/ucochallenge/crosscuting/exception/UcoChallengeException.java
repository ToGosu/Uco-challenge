package co.edu.uco.ucochallenge.crosscuting.exception;

public class UcoChallengeException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public UcoChallengeException(String message) {
        super(message);
    }
    
    public UcoChallengeException(String message, Throwable cause) {
        super(message, cause);
    }
}