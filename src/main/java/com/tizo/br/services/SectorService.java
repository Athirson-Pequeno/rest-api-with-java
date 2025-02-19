package com.tizo.br.services;

import com.tizo.br.model.Sector;
import com.tizo.br.repositories.SectorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SectorService {

    @Autowired
    SectorRepository sectorRepository;

    public Sector create(@Valid Sector sector) {
        return sectorRepository.save(sector);
    }

    public Page<Sector> findAll(Pageable pageable) {
        return sectorRepository.findAll(pageable);
    }

    public void delete(Long id) {
        sectorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Sector not found"));
        sectorRepository.deleteById(id);
    }

    public Sector update(@Valid Sector sector) {
        sectorRepository.findById(sector.getId()).orElseThrow(() ->
                new EntityNotFoundException("Sector not found"));
        return sectorRepository.save(sector);
    }

    public Sector findById(Long id) {
        return sectorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Sector not found"));
    }
}
