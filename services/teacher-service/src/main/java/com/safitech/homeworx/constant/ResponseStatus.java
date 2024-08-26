package com.safitech.homeworx.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {
    OPERATION_SUCCESS(200),
    ENTITY_CREATED(201),
    NO_CONTENT(204),
    NOT_MODIFIED(304),
    INVALID_REQUEST(400),
    UNAUTHORIZED_ACCESS(401),
    FORBIDDEN_ACCESS(403),
    RESOURCE_NOT_FOUND(404),
    SERVER_ERROR(500);

    private final int httpCode;
}