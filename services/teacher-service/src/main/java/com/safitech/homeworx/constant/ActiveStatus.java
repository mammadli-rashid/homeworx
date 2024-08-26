package com.safitech.homeworx.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActiveStatus {
    ACTIVE(1),
    DELETED(0);

    private final int value;
}
