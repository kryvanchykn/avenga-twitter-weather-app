package com.example.avengatwitterweatherapp.constraintValidator;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.service.RegionService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckedRegionsIdListConstraintValidator implements ConstraintValidator<RegionsIdListConstraint, List<Long>> {
    private static final Logger log = LogManager.getLogger(CheckedRegionsIdListConstraintValidator.class);
    private final RegionService regionService;

    public CheckedRegionsIdListConstraintValidator(RegionService regionService) {
        this.regionService = regionService;
    }

    @Override
    public boolean isValid(List<Long> regionsId, ConstraintValidatorContext context) {
        if(regionsId == null){
            return true;
        }

        Set<Long> regionsIdFromDB = regionService.getAllRegions().stream().map(Region::getId).collect(Collectors.toSet());
        return regionsIdFromDB.containsAll(regionsId);
    }
}