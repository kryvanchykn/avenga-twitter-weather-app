package com.example.avengatwitterweatherapp.constraintValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumConstraintValidator implements ConstraintValidator<EnumConstraint, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(EnumConstraint annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::toString)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return acceptedValues.contains(value.toString());
    }
}