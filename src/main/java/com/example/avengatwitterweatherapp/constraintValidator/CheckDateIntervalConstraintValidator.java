package com.example.avengatwitterweatherapp.constraintValidator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.Arrays;

public class CheckDateIntervalConstraintValidator implements ConstraintValidator<CheckDateIntervalConstraint, Object> {
    private static final Logger log = LogManager.getLogger(CheckDateIntervalConstraintValidator.class);

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            LocalDateTime sinceDate = (LocalDateTime) Arrays.stream(value.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Since.class))
                    .findAny()
                    .orElseThrow()
                    .get(value);
            LocalDateTime untilDate = (LocalDateTime) Arrays.stream(value.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Until.class))
                    .findAny()
                    .orElseThrow()
                    .get(value);

            return sinceDate.isBefore(untilDate);

        } catch (IllegalAccessException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
