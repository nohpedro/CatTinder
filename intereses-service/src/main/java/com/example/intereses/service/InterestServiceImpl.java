package com.example.intereses.service;

import com.example.intereses.dto.InterestDTO;
import com.example.intereses.exception.InterestNotFoundException;
import com.example.intereses.model.Interest;
import com.example.intereses.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {
    
    private final InterestRepository interestRepository;
    
    @Override
    @Transactional
    public Interest createInterest(InterestDTO interestDTO) {
        Interest interest = new Interest();
        interest.setNombre(interestDTO.getNombre());
        interest.setDescripcion(interestDTO.getDescripcion());
        interest.setUserId(interestDTO.getUserId());
        return interestRepository.save(interest);
    }
    
    @Override
    public Interest getInterestById(Long id) {
        return interestRepository.findById(id)
                .orElseThrow(() -> new InterestNotFoundException("Interés no encontrado con id: " + id));
    }
    
    @Override
    public List<Interest> getAllInterests() {
        return interestRepository.findAll();
    }
    
    @Override
    public List<Interest> getInterestsByUserId(Long userId) {
        return interestRepository.findByUserId(userId);
    }
    
    @Override
    public List<Interest> searchInterestsByName(String nombre) {
        return interestRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    @Transactional
    public Interest updateInterest(Long id, InterestDTO interestDTO) {
        Interest interest = getInterestById(id);
        interest.setNombre(interestDTO.getNombre());
        interest.setDescripcion(interestDTO.getDescripcion());
        interest.setUserId(interestDTO.getUserId());
        return interestRepository.save(interest);
    }
    
    @Override
    @Transactional
    public void deleteInterest(Long id) {
        Interest interest = getInterestById(id);
        interestRepository.delete(interest);
    }
    
    @Override
    public boolean tienenInteresesEnComun(Long userId1, Long userId2) {
        return interestRepository.tienenInteresesEnComun(userId1, userId2);
    }
    
    @Override
    public List<Interest> encontrarInteresesCompartidos(Long userId1, Long userId2) {
        return interestRepository.encontrarInteresesCompartidos(userId1, userId2);
    }
}

