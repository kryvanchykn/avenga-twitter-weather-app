package com.example.avengatwitterweatherapp.constraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckDateIntervalConstraintValidator.class)
public @interface CheckDateIntervalConstraint
{
    String sinceDateStr();
    String untilDateStr();
    String message() default "sinceDate should be before untilDate";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
