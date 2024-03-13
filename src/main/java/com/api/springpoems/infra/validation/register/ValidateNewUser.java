package com.api.springpoems.infra.validation.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.springpoems.dto.user.RegisterUserData;
import com.api.springpoems.infra.exceptions.ValidationException;
import com.api.springpoems.repositories.UserRepository;

@Component
public class ValidateNewUser implements RegisterValidator {
    @Autowired
    private UserRepository repository;

    public void validate(RegisterUserData data) {
        if (repository.findByUsername(data.username()) != null) {
            throw new ValidationException("Username already exists!");
        }
    }
}
