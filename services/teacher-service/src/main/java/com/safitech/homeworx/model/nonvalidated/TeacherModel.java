package com.safitech.homeworx.model.nonvalidated;

import com.safitech.homeworx.constant.ContactType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Representation of a teacher model")
public class TeacherModel {

    @Schema(description = "Full name of the teacher", example = "John Doe")
    String fullName;

    @Schema(description = "Contact details of the teacher", example = "{ \"EMAIL\": \"john.doe@example.com\", \"MOBILE\": \"123-456-7890\" }")
    Map<ContactType, String> contacts;

    @Schema(description = "Date of birth of the teacher", example = "1980-01-15")
    LocalDate birthdate;

    @Schema(description = "List of subjects taught by the teacher", example = "[\"Mathematics\", \"Physics\"]")
    Set<String> teachingSubjects;

    @Schema(description = "List of education history details")
    Set<EducationDetail> educationHistory;

    @Schema(description = "List of career history details")
    Set<CareerDetail> workHistory;
}
