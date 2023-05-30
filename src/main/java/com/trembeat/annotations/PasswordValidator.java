package com.trembeat.annotations;

import com.trembeat.domain.viewmodels.UserEditViewModel;
import com.trembeat.domain.viewmodels.UserRegisterViewModel;
import jakarta.validation.*;


public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {
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
        return user.getPassword().compareTo(user.getPasswordConfirmation()) == 0;
    }

    private boolean checkEditViewModel(Object o) {
        UserEditViewModel user = (UserEditViewModel)o;
        return (user.getPassword() == null || user.getPassword().isEmpty())
                || user.getPassword().compareTo(user.getPasswordConfirmation()) == 0;
    }
}
