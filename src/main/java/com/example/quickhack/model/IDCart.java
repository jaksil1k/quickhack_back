package com.example.quickhack.model;

import jakarta.persistence.Column;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class IDCart {
    private String name;
    private String iin;
    private String number;
    private String nationality;
    private String issuingAuthority;
    @DateTimeFormat(pattern = "MM/yyyy")
    private Date expirationDate;
}
