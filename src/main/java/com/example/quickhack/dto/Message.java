package com.example.quickhack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Message {
    private String role;
    private String content;//prompt
    private List<FileAttachment> attachments;
}
