package com.trembeat.annotations;

import com.trembeat.domain.viewmodels.*;
import jakarta.validation.*;

import java.util.regex.Pattern;


public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    private Pattern _pattern;
    private static final String _passwordRegex = "^[a-zA-Z0-9!@#$%^&*()\\-_=\\.]{4,}$";


    public PasswordValidator() {
        _pattern = Pattern.compile(_passwordRegex);
    }

    @Override
    public void initialize(ValidPassword annotation) {
        ConstraintValidator.super.initialize(annotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
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
        return _pattern.matcher(password).matches();
    }
}
