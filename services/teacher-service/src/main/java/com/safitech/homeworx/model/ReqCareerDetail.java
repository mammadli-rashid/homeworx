package com.safitech.homeworx.model;

import com.safitech.homeworx.validation.annotation.ValidStartEndDates;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Career detail of the teacher")
public class ReqCareerDetail {

    @NotBlank(message = "Workplace name must not be blank.")
    @Schema(description = "Name of the workplace", example = "ABC University")
    String workplace;

    @NotBlank(message = "Occupation name must not be blank.")
    @Schema(description = "Occupation name", example = "Professor")
    String occupation;

    @NotNull(message = "Start date is required.")
    @PastOrPresent(message = "Start date must be in the past or present.")
    @Schema(description = "Start date of the career", example = "2015-09-01")
    LocalDate startDate;

    @Schema(description = "End date of the career", example = "2020-05-15")
    LocalDate endDate;
}
