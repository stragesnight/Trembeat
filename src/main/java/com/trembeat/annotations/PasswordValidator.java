package com.trembeat.annotations;

import com.trembeat.domain.viewmodels.UserViewModel;
import jakarta.validation.*;


public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        UserViewModel user = (UserViewModel)o;
        return user != null && user.getPassword().compareTo(user.getPasswordConfirmation()) == 0;
    }
}
