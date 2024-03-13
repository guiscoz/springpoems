package com.api.springpoems.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.springpoems.repositories.UserRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recoverToken(request);

        if (tokenJWT != null) {
            try {
                var subject = tokenService.getSubject(tokenJWT);
                var user = repository.findByUsername(subject);
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // Log de detalhes do token e do usuário
                logger.info("Token JWT: " + tokenJWT);
                logger.info("Usuário autenticado: " + user.getUsername());
                
            } catch (JWTVerificationException exception) {
                logger.error("Invalid or expired token JWT!: " + tokenJWT, exception);
                throw new RuntimeException("Invalid or expired token JWT!");
            } catch (Exception e) {
                logger.error("Error on retrieving user from token: " + tokenJWT, e);
                throw new RuntimeException("Error retrieving user from token!");
            }
        }
    
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

       return null;
    }
}