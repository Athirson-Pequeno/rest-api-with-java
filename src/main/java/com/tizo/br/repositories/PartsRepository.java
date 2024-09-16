package com.tizo.br.repositories;

import com.tizo.br.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartsRepository extends JpaRepository<Part, Long> {
}
