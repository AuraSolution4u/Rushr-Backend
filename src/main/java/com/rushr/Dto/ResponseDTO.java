package com.rushr.Dto;

import lombok.Data;

@Data
public class ResponseDTO {

    private String message;

    private Long statusCode;

    private Object responseObject;

}
