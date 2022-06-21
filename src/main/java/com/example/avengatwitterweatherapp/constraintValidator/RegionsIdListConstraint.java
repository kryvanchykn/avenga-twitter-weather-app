package com.example.avengatwitterweatherapp.constraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = CheckedRegionsIdListConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegionsIdListConstraint {
    String message() default "The input list should contain valid regions' id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}