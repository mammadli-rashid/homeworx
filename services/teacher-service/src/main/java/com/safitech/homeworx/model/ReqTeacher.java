package com.safitech.homeworx.model;

import com.safitech.homeworx.constant.ContactType;
import com.safitech.homeworx.validation.annotation.EmailExists;
import com.safitech.homeworx.validation.annotation.MobileNumberExists;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Teacher's request information")
public class ReqTeacher {

    @NotBlank(message = "Teacher's name cannot be empty!")
    @Schema(description = "Full name of the teacher", example = "John Doe")
    String fullName;

    @NotNull(message = "Contact details cannot be null!")
    @EmailExists
    @MobileNumberExists
    @Schema(description = "Map of contact details")
    Map<ContactType, @NotBlank(message = "Contact detail cannot be blank!") String> contacts;

    @NotNull(message = "Birth date cannot be null!")
    @Schema(description = "Birthdate of the teacher", example = "1980-01-15")
    LocalDate birthdate;

    @NotEmpty(message = "Teaching subjects cannot be empty!")
    @Schema(description = "Set of teaching subjects", example = "[\"Mathematics\", \"Physics\"]")
    Set<String> teachingSubjects;

    @Valid
    @Schema(description = "Set of education details")
    Set<ReqEducationDetail> educationHistory;

    @Valid
    @Schema(description = "Set of career details")
    Set<ReqCareerDetail> workHistory;
}
