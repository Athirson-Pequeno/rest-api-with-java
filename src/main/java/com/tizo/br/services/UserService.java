package com.tizo.br.services;

import com.tizo.br.model.Permission;
import com.tizo.br.model.User;
import com.tizo.br.repositories.PermissionRepository;
import com.tizo.br.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Service
public class UserService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    PermissionRepository permissionRepository;

    public User createUser(User user) {

        //Criptografa a senha antes de salvar no banco de dados
        Map<String, PasswordEncoder> encoderMap = new HashMap<>();
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                //Senha, ps: senha temporária
                "tizo",
                //Tamanho do valor aleatório aplicado em cada interação
                8,
                //Número de interações
                185000,
                //Algoritmo de encriptação
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        encoderMap.put("pbkdf2", pbkdf2PasswordEncoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoderMap);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        /*user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);*/

        List<Permission> permissions = new ArrayList<>();
        permissions.add(permissionRepository.findById(3L).stream().findFirst().orElse(new Permission(99L, "ERRO")));
        user.setPermissions(permissions);
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        logger.info("Finding one user by email " + email + "!");
        var user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException(email + " not found!");
        }
    }
}
