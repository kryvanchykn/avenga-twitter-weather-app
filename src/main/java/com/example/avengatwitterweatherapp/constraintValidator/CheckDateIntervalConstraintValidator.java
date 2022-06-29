package com.example.avengatwitterweatherapp.constraintValidator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.STRIKE_DATE_FORMATTER;

public class CheckDateIntervalConstraintValidator implements ConstraintValidator<CheckDateIntervalConstraint, Object>
{
    private static final Logger log = LogManager.getLogger(CheckDateIntervalConstraintValidator.class);
    private String sinceDateStr;
    private String untilDateStr;

    @Override
    public void initialize(final CheckDateIntervalConstraint constraintAnnotation)
    {
        sinceDateStr = constraintAnnotation.sinceDateStr();
        untilDateStr = constraintAnnotation.untilDateStr();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
        try {
            LocalDateTime sinceDate = LocalDateTime.parse(BeanUtils.getProperty(value, sinceDateStr), STRIKE_DATE_FORMATTER);
            LocalDateTime untilDate = LocalDateTime.parse(BeanUtils.getProperty(value, untilDateStr), STRIKE_DATE_FORMATTER);
            return sinceDate.isBefore(untilDate);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
