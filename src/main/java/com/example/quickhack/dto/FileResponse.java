package com.example.quickhack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FileResponse implements Serializable {
    private String id;
    private String object;
    private Integer bytes;
    private Long created_at;
    private String filename;
    private String purpose;
}
