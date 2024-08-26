package com.safitech.homeworx.mapper;

import com.safitech.homeworx.model.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper<T> {
    public ResponseEntity<T> toResponseEntity(Response<T> response) {
        return new ResponseEntity<>(response.getData(),
                HttpStatusCode.valueOf(response.getStatus().getHttpCode()));
    }
}
