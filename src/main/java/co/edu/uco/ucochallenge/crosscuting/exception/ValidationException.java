package co.edu.uco.ucochallenge.crosscuting.exception;

public class ValidationException extends UcoChallengeException {
    
    private static final long serialVersionUID = 1L;
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
