package com.example.avengatwitterweatherapp.dto;

import com.example.avengatwitterweatherapp.constraintValidator.*;
import com.example.avengatwitterweatherapp.model.SortDir;
import com.example.avengatwitterweatherapp.model.SortField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.ASC_ORDER;
import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.SORT_BY_REGION;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.BOUNDARY_DATE;

@Data
@CheckDateIntervalConstraint
public class RocketStrikeDto {
    @Since
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent
    public LocalDateTime sinceDate = BOUNDARY_DATE;

    @Until
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent
    public LocalDateTime untilDate = LocalDateTime.now();

    @NotNull
    @RegionsIdListConstraint
    private List<Long> checkedRegionsId;

    @EnumConstraint(enumClass = SortField.class)
    private String sortField = SORT_BY_REGION;

    @EnumConstraint(enumClass = SortDir.class)
    private String sortDir = ASC_ORDER;
}
