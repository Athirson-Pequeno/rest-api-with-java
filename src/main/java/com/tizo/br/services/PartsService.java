package com.tizo.br.services;

import com.tizo.br.model.Part;
import com.tizo.br.model.Sector;
import com.tizo.br.model.Type;
import com.tizo.br.repositories.PartsRepository;
import com.tizo.br.repositories.SectorRepository;
import com.tizo.br.repositories.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartsService {

    @Autowired
    PartsRepository partsRepository;

    @Autowired
    SectorRepository sectorRepository;

    @Autowired
    TypeRepository typeRepository;

    public Part createPart(Part part) {
        List<Sector> sectors = new ArrayList<>();
        for (Sector s : part.getSectors()) {
            sectors.add(sectorRepository.findById(s.getId()).orElseThrow(() -> new EntityNotFoundException("Setor com ID " + s.getId() + " não encontrado.")));
        }
        part.setSectors(sectors);

        List<Type> types = new ArrayList<>();
        for (Type t : part.getTypes()) {
            types.add(typeRepository.findById(t.getId()).orElseThrow(() -> new EntityNotFoundException("Tipo com ID " + t.getId() + " não encontrado.")));
        }
        part.setTypes(types);

        return partsRepository.save(part);
    }

    public Page<Part> findAll(Pageable pageable) {
        return partsRepository.findAll(pageable);
    }

    public void delete(Long id) {
        partsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Part not found"));
        partsRepository.deleteById(id);
    }

    public Part update(Part part) {
        partsRepository.findById(part.getId()).orElseThrow(() ->
                new EntityNotFoundException("Part not found"));
        return partsRepository.save(part);
    }

    public Part findById(Long id) {
        return partsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Part not found"));
    }
}
