package com.safitech.homeworx.model;

import com.safitech.homeworx.constant.EducationLevel;
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
@Schema(description = "Education detail of the teacher")
public class ReqEducationDetail {

    @NotBlank(message = "Institution name must not be blank.")
    @Schema(description = "Name of the educational institution", example = "XYZ University")
    String institution;

    @NotBlank(message = "Faculty name must not be blank.")
    @Schema(description = "Name of the faculty", example = "Engineering")
    String faculty;

    @NotBlank(message = "Specialization name must not be blank.")
    @Schema(description = "Specialization name", example = "Computer Science")
    String specialization;

    @NotNull(message = "Education level is required.")
    @Schema(description = "Level of education", example = "MASTER")
    EducationLevel educationLevel;

    @NotNull(message = "Start date is required.")
    @PastOrPresent(message = "Start date must be in the past or present.")
    @Schema(description = "Start date of the education", example = "2010-09-01")
    LocalDate startDate;

    @Schema(description = "End date of the education", example = "2015-06-30")
    LocalDate endDate;
}
