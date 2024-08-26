package com.safitech.homeworx.model.nonvalidated;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Details about a teacher's career")
public class CareerDetail {

    @Schema(description = "Name of the workplace", example = "ABC University")
    String workplace;

    @Schema(description = "Occupation at the workplace", example = "Professor")
    String occupation;

    @Schema(description = "Start date of employment", example = "2015-09-01")
    LocalDate startDate;

    @Schema(description = "End date of employment", example = "2020-05-15")
    LocalDate endDate;
}
