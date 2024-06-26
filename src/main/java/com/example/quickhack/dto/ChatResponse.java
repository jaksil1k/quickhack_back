package com.example.quickhack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatResponse {
    private String status;
    private IDCardGPTResponse data;
    private String message;

    public ChatResponse(String status) {
        this.status = status;
    }
}

