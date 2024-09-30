package com.tizo.br.services;

import com.tizo.br.model.vo.security.AccountCredentialsVO;
import com.tizo.br.model.vo.security.TokenVO;
import com.tizo.br.repositories.UserRepository;
import com.tizo.br.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthServices {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    private final Logger logger = Logger.getLogger(AuthServices.class.getName());

    @SuppressWarnings("rawtypes")
    public ResponseEntity signIn(AccountCredentialsVO data) {

        try {
            var email = data.email();
            var password = data.password();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            var user = repository.findByEmail(email);

            var tokenResponse = new TokenVO();

            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(email, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Email " + email + " not found!");
            }

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new BadCredentialsException("Invalid email/password supplied!");
        }
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refreshToken(String email, String refreshToken) {

        var user = repository.findByEmail(email);
        var tokenResponse = new TokenVO();

        if (user != null) {
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + email + " not found!");
        }
        return ResponseEntity.ok(tokenResponse);

    }
}
