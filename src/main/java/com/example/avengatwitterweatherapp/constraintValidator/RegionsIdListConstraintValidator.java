package com.example.avengatwitterweatherapp.constraintValidator;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.service.RegionService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RegionsIdListConstraintValidator implements ConstraintValidator<RegionsIdListConstraint, List<Long>> {
    private final RegionService regionService;

    public RegionsIdListConstraintValidator(RegionService regionService) {
        this.regionService = regionService;
    }

    @Override
    public boolean isValid(List<Long> regionsId, ConstraintValidatorContext context) {
        if (regionsId != null) {
            Set<Long> regionsIdFromDB = regionService.getAllRegions().stream().map(Region::getId).collect(Collectors.toSet());
            return regionsIdFromDB.containsAll(regionsId);
        }
        return true;
    }
}