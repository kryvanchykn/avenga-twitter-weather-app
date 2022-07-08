package com.example.avengatwitterweatherapp.constraintValidator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

public class CheckDateIntervalConstraintValidator implements ConstraintValidator<CheckDateIntervalConstraint, Object> {
    private static final Logger log = LogManager.getLogger(CheckDateIntervalConstraintValidator.class);

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            Optional<Field> sinceField = Arrays.stream(value.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Since.class))
                    .findAny();
            sinceField.ifPresent(field -> field.setAccessible(true));
            LocalDateTime sinceDate = (LocalDateTime) sinceField.orElseThrow().get(value);

            Optional<Field> untilField = Arrays.stream(value.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Until.class))
                    .findAny();
            untilField.ifPresent(field -> field.setAccessible(true));
            LocalDateTime untilDate = (LocalDateTime) untilField.orElseThrow().get(value);

            return sinceDate.isBefore(untilDate);

        } catch (IllegalAccessException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
