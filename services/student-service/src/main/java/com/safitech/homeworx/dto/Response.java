package com.safitech.homeworx.dto;

import com.safitech.homeworx.constant.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    T data;
    ResponseStatus status;
}
