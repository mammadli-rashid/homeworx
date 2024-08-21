package com.safitech.homeworx.dto;

import com.safitech.homeworx.validation.annotation.UniqueStringCollectionIgnoreCase;
import com.safitech.homeworx.validation.annotation.ValidSortCriteria;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ValidSortCriteria(message = "There is a problem with sort criteria. Please correct it.")
public class ReqSpecPaginationSorting {
    String fullName;
    LocalDate dateOfBirth;
    String email;
    String primaryMobileNumber;
    String secondaryMobileNumber;
    String address;
    @Min(value = 0, message = "Offset can not be smaller than 0!")
    Integer offset;
    @Min(value = 1, message = "Page size can not be smaller than 1!")
    Integer pageSize;
    @UniqueStringCollectionIgnoreCase(message = "Sort fields must be unique!")
    List<String> sortFields;
    List<String> sortDirections;
}
