package com.tizo.br.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tizo.br.exceptions.InvalidJwtAuthenticationException;
import com.tizo.br.model.vo.security.TokenVO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;


@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private final long validateInMilliseconds = 3600000; // 1h

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    //Inicia antes de qualquer outro método
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenVO refreshToken(String refreshToken) {

        if (refreshToken.contains("Bearer ")) refreshToken = refreshToken.substring("Bearer ".length());

        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

        return createAccessToken(username, roles);
    }

    public TokenVO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date validate = new Date(now.getTime() + validateInMilliseconds);
        var accessToken = getAccessToken(username, roles, now, validate);
        var refreshToken = getRefreshToken(username, roles, now);
        return new TokenVO(username, true, now, validate, accessToken, refreshToken);
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {

        Date validateRefreshToken = new Date(now.getTime() + (validateInMilliseconds * 3));

        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validateRefreshToken)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();

    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validate) {

        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                //níveis de acesso que esse usuário tem
                .withClaim("roles", roles)
                //momento que o token foi gerado
                .withIssuedAt(now)
                //data de expiração
                .withExpiresAt(validate)
                //nome do usuário que solicitou o token
                .withSubject(username)
                //emissor do token
                .withIssuer(issuerUrl)
                //algoritmo de criptografia usado
                .sign(algorithm)
                //remove caracteres indesejados do token, como espaços em branco adicionais
                .strip();

    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    private DecodedJWT decodedToken(String token) {

        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier jwtVerifier = JWT.require(alg).build();

        return jwtVerifier.verify(token);

    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null) {
            return bearerToken.replace("Bearer ", "");
        }
        return null;
    }

    public Boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }
}
