package com.tizo.br.repositories;

import com.tizo.br.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    @Query("SELECT s FROM Sector s WHERE s.description =:description")
    Sector findByDescription(@Param("description") String description);

}