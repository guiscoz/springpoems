package com.api.springpoems.infra.validation.register;

import org.springframework.stereotype.Component;

import com.api.springpoems.dto.user.RegisterUserData;
import com.api.springpoems.infra.exceptions.ValidationException;

@Component
public class ValidatePassword implements RegisterValidator {
    public void validate(RegisterUserData data) {
        if (!data.password().equals(data.confirmPassword())) {
            throw new ValidationException("The password and the it's confirmation do not match.");
        }
    }
}
