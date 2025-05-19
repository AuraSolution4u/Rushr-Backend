package com.rushr.Dto;

import lombok.Data;

@Data
public class EmailDetailsDTO {

    private String recipient;

    private String msgBody;

    private String subject;

    private String attachment;

}
