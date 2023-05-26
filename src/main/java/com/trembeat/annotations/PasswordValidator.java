package com.trembeat.annotations;

import com.trembeat.domain.models.User;
import jakarta.validation.*;


public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        User user = (User)o;
        return user != null && user.getPassword().compareTo(user.getPasswordConfirmation()) == 0;
    }
}
