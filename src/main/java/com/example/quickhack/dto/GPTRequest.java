package com.example.quickhack.dto;

import com.example.quickhack.utils.MessageUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GPTRequest {
    private String model;
    private List<Message> messages;

    public GPTRequest(String model, String prompt, String fileId) {
        this.model = model;
        this.messages = new ArrayList<>();
        List<FileAttachment> attachments = new ArrayList<>();
        List<Tool> tools = new ArrayList<>();
        tools.add(new Tool("file_search"));
        attachments.add(new FileAttachment(fileId, tools));
        this.messages.add(new Message("user", prompt, attachments));
    }

    public GPTRequest(String model, String fileId) {
        this(model, fileId, MessageUtil.SYSTEM_PROMPT);
    }
}
