package com.yaryy.post_service.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMapper {

    public static <T> ResponseEntity<T> badRequest(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    public static <T> ResponseEntity<T> notFoundRequest(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
