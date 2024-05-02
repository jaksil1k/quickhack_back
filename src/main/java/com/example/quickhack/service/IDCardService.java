package com.example.quickhack.service;

import com.example.quickhack.model.IDCard;
import com.example.quickhack.repository.IDCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IDCardService {
    private final IDCardRepository idCardRepository;
    public Optional<IDCard> getById(String iin) {
        return idCardRepository.findById(iin);
    }
}
