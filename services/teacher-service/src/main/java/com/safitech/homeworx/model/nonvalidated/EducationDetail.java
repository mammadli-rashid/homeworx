package com.safitech.homeworx.model.nonvalidated;

import com.safitech.homeworx.constant.EducationLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Details about a teacher's education")
public class EducationDetail {

    @Schema(description = "Name of the educational institution", example = "XYZ University")
    String institution;

    @Schema(description = "Name of the faculty", example = "Engineering")
    String faculty;

    @Schema(description = "Field of specialization", example = "Computer Science")
    String specialization;

    @Schema(description = "Level of education", example = "MASTER")
    EducationLevel educationLevel;

    @Schema(description = "Start date of education", example = "2010-09-01")
    LocalDate startDate;

    @Schema(description = "End date of education", example = "2015-06-30")
    LocalDate endDate;
}
