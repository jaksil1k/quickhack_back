package com.example.quickhack.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BaseResponse {
    private String statusCode;
    private String message;
}
