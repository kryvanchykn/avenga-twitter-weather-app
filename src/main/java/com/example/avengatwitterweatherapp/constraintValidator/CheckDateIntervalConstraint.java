package com.example.avengatwitterweatherapp.constraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckDateIntervalConstraintValidator.class)
public @interface CheckDateIntervalConstraint {
    String message() default "sinceDate should be before untilDate";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
