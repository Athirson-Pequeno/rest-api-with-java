package com.tizo.br.repositories;

import com.tizo.br.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username =:username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.email =:email")
    User findByEmail(@Param("email") String email);
}
