package com.example.avengatwitterweatherapp.dto;

import com.example.avengatwitterweatherapp.constraintValidator.RegionsIdListConstraint;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class RocketStrikeDto {
    @Pattern(regexp = "2021-(?:0[1-9]|1[012])-(?:0[1-9]|[12][0-9]|3[01])T([0-1][0-9]|2[0-3]):([0-5][0-9])|null",
            message = "sinceDate should match pattern yyyy-MM-dd'T'HH:mm")
    String sinceDate;

    @Pattern(regexp = "2022-(?:0[1-9]|1[012])-(?:0[1-9]|[12][0-9]|3[01])T([0-1][0-9]|2[0-3]):([0-5][0-9])|null",
            message = "untilDate should match pattern yyyy-MM-dd'T'HH:mm")
    String untilDate;

//    @Pattern(regexp = "\\[(?:\\d+,\\s*)+\\d+]")
    @RegionsIdListConstraint
    List<Long> checkedRegionsId;

    @Pattern(regexp = "region|strikeDate|null", message = "sortField should be region or strikeDate only")
    String sortField;

    @Pattern(regexp = "asc|desc|null", message = "sortDir should be asc or desc only")
    String sortDir;
}
