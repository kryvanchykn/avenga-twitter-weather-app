package com.example.avengatwitterweatherapp.dto;

import com.example.avengatwitterweatherapp.constraintValidator.CheckDateIntervalConstraint;
import com.example.avengatwitterweatherapp.constraintValidator.EnumConstraint;
import com.example.avengatwitterweatherapp.constraintValidator.RegionsIdListConstraint;
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
@CheckDateIntervalConstraint(sinceDateStr = "sinceDate", untilDateStr = "untilDate")
public class RocketStrikeDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent
    LocalDateTime sinceDate = BOUNDARY_DATE;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent
    LocalDateTime untilDate = LocalDateTime.now();

    @NotNull
    @RegionsIdListConstraint
    List<Long> checkedRegionsId;

    @EnumConstraint(enumClass = SortField.class)
    String sortField = SORT_BY_REGION;

    @EnumConstraint(enumClass = SortDir.class)
    String sortDir = ASC_ORDER;
}
