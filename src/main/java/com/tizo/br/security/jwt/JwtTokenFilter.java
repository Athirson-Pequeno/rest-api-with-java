package com.tizo.br.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //separa o token de autorização do header da requisição
        String token = tokenProvider.resolveToken((HttpServletRequest) request);
        //verifica se o token não é nulo e se ele é válido e está em dia
        if (token != null && tokenProvider.validateToken(token)) {
            //busca detalhes do usuário autenticado
            Authentication authentication = tokenProvider.getAuthentication(token);
            if (authentication != null) {
                //autentica o usuário no contexto de segurança da API
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
