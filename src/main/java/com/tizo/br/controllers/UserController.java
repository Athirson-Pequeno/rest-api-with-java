package com.tizo.br.controllers;

import com.tizo.br.model.User;
import com.tizo.br.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users/v1")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        return ResponseEntity.ok(service.findAllUsers());
    }

    @PostMapping("/recordUser/commonUser")
    public ResponseEntity<User> createCommonUser(@RequestBody User user) {
        List<Long> authorities = new ArrayList<>();
        authorities.add(3L);
        return ResponseEntity.ok(service.createUser(user, authorities));
    }

    @PostMapping("/recordUser/managerUser")
    public ResponseEntity<User> createManager(@RequestBody User user) {
        List<Long> authorities = new ArrayList<>();
        authorities.add(2L);
        authorities.add(3L);
        return ResponseEntity.ok(service.createUser(user, authorities));
    }
}
