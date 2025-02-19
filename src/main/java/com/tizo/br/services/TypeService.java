package com.tizo.br.services;

import com.tizo.br.model.Type;
import com.tizo.br.repositories.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TypeService {

    @Autowired
    TypeRepository typeRepository;

    public Type create(@Valid Type type) {
        return typeRepository.save(type);
    }

    public Page<Type> findAll(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    public void delete(Long id) {
        typeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Type not found"));
        typeRepository.deleteById(id);
    }

    public Type update(@Valid Type type) {
        typeRepository.findById(type.getId()).orElseThrow(() ->
                new EntityNotFoundException("Type not found"));
        return typeRepository.save(type);
    }

    public Type findById(Long id) {
        return typeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Type not found"));
    }
}
