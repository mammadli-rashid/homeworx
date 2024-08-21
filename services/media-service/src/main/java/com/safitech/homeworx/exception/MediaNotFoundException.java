package com.safitech.homeworx.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class MediaNotFoundException extends NotFoundException {

    public MediaNotFoundException(long id) {
        super("Media not found with this id: " + id);
    }

}
