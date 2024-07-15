package com.tizo.br.repositories;

import com.tizo.br.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TypeRepository extends JpaRepository<Type, Long> {

    @Query("SELECT t FROM Type t WHERE t.description =:description")
    Type findByDescription(@Param("description") String description);
}