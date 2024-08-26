package com.safitech.homeworx.exception;

import com.safitech.homeworx.constant.ContactType;
import com.safitech.homeworx.utility.StringUtils;
import org.springframework.http.HttpStatus;

public class DuplicateContactException extends CustomException {
    public DuplicateContactException(ContactType contactType, String contactDetail) {
        super(buildMessage(contactType, contactDetail), HttpStatus.BAD_REQUEST);
    }

    private static String buildMessage(ContactType contactType, String contactDetail) {
        return (StringUtils.toInitCap(contactType.name())) + " already exists with value: " + contactDetail;
    }
}
