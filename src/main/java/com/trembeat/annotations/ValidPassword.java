package com.trembeat.annotations;

import jakarta.validation.*;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Passwords should match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
