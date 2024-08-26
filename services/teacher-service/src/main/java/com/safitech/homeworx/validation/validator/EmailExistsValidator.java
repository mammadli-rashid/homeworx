package com.safitech.homeworx.validation.validator;

import com.safitech.homeworx.constant.ContactType;
import com.safitech.homeworx.validation.annotation.EmailExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

public class EmailExistsValidator implements ConstraintValidator<EmailExists, Map<ContactType, String>> {

    @Override
    public boolean isValid(Map<ContactType, String> contacts, ConstraintValidatorContext context) {
        return contacts.containsKey(ContactType.EMAIL);
    }

}
