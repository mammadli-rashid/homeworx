package com.safitech.homeworx.model;

import com.safitech.homeworx.constant.ResponseStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response<T> {
    T data;
    ResponseStatus status;
}
