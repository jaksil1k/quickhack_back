package com.example.quickhack.repository;

import com.example.quickhack.model.IDCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDCardRepository extends JpaRepository<IDCard, String> {
}
