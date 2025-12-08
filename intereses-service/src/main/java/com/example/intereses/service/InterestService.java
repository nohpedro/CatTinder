package com.example.intereses.service;

import com.example.intereses.dto.InterestDTO;
import com.example.intereses.model.Interest;

import java.util.List;

public interface InterestService {
    Interest createInterest(InterestDTO interestDTO);
    Interest getInterestById(Long id);
    List<Interest> getAllInterests();
    List<Interest> getInterestsByUserId(Long userId);
    List<Interest> searchInterestsByName(String nombre);
    Interest updateInterest(Long id, InterestDTO interestDTO);
    void deleteInterest(Long id);
    boolean tienenInteresesEnComun(Long userId1, Long userId2);
    List<Interest> encontrarInteresesCompartidos(Long userId1, Long userId2);
}

