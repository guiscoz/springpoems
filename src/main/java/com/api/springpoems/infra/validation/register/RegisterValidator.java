package com.api.springpoems.infra.validation.register;

import org.springframework.stereotype.Component;

import com.api.springpoems.dto.user.RegisterUserData;

@Component
public interface RegisterValidator {
    void validate (RegisterUserData data);   
}
