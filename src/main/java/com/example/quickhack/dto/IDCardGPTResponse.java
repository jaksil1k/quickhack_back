package com.example.quickhack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class IDCardGPTResponse {
    private String iin;
    private String full_name;
    private String birthday;
    private String card_id;
    private String given_date;
    private String expiration_date;
}
