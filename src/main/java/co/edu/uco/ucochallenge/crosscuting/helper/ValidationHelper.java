package co.edu.uco.ucochallenge.crosscuting.helper;

import java.util.regex.Pattern;

import co.edu.uco.ucochallenge.crosscuting.exception.ValidationException;

public final class ValidationHelper {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern MOBILE_PATTERN = Pattern.compile(
        "^\\+?[0-9]{10,15}$"
    );

    private ValidationHelper() {
    }

    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("El correo electrónico es obligatorio");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("El correo electrónico no tiene un formato válido");
        }
    }

    public static void validateMobileNumber(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.isBlank()) {
            throw new ValidationException("El número de celular es obligatorio");
        }
        
        if (!MOBILE_PATTERN.matcher(mobileNumber).matches()) {
            throw new ValidationException(
                "El número de celular debe tener entre 10 y 15 dígitos"
            );
        }
    }

    public static void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " es obligatorio");
        }
    }

    public static void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new ValidationException(
                fieldName + " no puede tener más de " + maxLength + " caracteres"
            );
        }
    }
}
