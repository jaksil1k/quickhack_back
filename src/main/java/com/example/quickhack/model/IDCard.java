package com.example.quickhack.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "idcard")
public class IDCard {
    @Id
    private String iin;
    @Column(name = "full_name")
    private String fullName;
    private String birthday;
    @Column(name = "card_id")
    private String cardId;
    @Column(name = "given_date")
    private String givenDate;
    @Column(name = "expiration_date")
    private String expirationDate;
}
