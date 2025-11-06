package co.edu.uco.ucochallenge.crosscuting.exception.user;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeException;

public class UserAlreadyExistsException extends UcoChallengeException {
    
    private static final long serialVersionUID = 1L;
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
