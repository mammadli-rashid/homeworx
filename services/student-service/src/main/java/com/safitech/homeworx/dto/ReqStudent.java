package com.safitech.homeworx.dto;

import com.safitech.homeworx.validation.annotation.MobileNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqStudent {

    @NotBlank(message = "Full name can not be blank!")
    @Schema(description = "Full name of the student", example = "Rashid Mammadli")
    String fullName;

    @NotNull(message = "Birth date can not be null!")
    @PastOrPresent(message = "Birth date can not be in the future!")
    @Schema(description = "Birth date of the student", example = "2004-09-09")
    LocalDate dateOfBirth;

    @Email(message = "Email format is not correct!")
    @Schema(description = "Email of the student", example = "rashid.mammadli@safitech.com")
    String email;

    @MobileNumber(message = "Mobile number format is not correct!")
    @Schema(description = "Primary mobile number of the student", example = "+994502110001")
    String primaryMobileNumber;

    @MobileNumber(message = "Mobile number format is not correct!")
    @Schema(description = "Secondary mobile number of the student", example = "+994502110002")
    String secondaryMobileNumber;

    @Schema(description = "Address of the student", example = "Azerbaijan, Baku, Azadlig Ave. 43B")
    String address;
}
