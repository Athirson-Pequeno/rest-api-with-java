package com.tizo.br.services;

import com.tizo.br.model.Sector;
import com.tizo.br.repositories.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {

    @Autowired
    SectorRepository sectorRepository;

    public Sector createSector(Sector sector) {
        
        return sectorRepository.save(sector);
    }

    public List<Sector> getAllSectors() {
        return sectorRepository.findAll();
    }
}
