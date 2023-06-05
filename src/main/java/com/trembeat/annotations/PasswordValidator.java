package com.trembeat.annotations;

import com.trembeat.domain.viewmodels.*;
import jakarta.validation.*;

import java.util.regex.Pattern;


public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    private Pattern pattern;
    private static final String passwordRegex = "^[a-zA-Z0-9!@#$%^&*()\\-_=\\.]{4,}$";


    public PasswordValidator() {
        pattern = Pattern.compile(passwordRegex);
    }

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null)
            return false;

        if (o instanceof UserRegisterViewModel)
            return checkRegisterViewModel(o);

        if (o instanceof UserEditViewModel)
            return checkEditViewModel(o);

        return false;
    }

    private boolean checkRegisterViewModel(Object o) {
        UserRegisterViewModel user = (UserRegisterViewModel)o;
        String password = user.getPassword();
        String confirmation = user.getPasswordConfirmation();

        return isValidPassword(password) && password.compareTo(confirmation) == 0;
    }

    private boolean checkEditViewModel(Object o) {
        UserEditViewModel user = (UserEditViewModel)o;
        String password = user.getPassword();
        String confirmation = user.getPasswordConfirmation();

        return (password == null || password.isEmpty())
                || (isValidPassword(password) && password.compareTo(confirmation) == 0);
    }

    private boolean isValidPassword(String password) {
        return pattern.matcher(password).matches();
    }
}
