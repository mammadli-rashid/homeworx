package com.safitech.homeworx.validation.validator;

import com.safitech.homeworx.constant.ContactType;
import com.safitech.homeworx.validation.annotation.MobileNumberExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

public class MobileNumberExistsValidator implements ConstraintValidator<MobileNumberExists, Map<ContactType, String>> {

    @Override
    public boolean isValid(Map<ContactType, String> contacts, ConstraintValidatorContext context) {
        return contacts.containsKey(ContactType.MOBILE);
    }

}
