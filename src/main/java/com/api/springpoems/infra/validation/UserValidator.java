package com.api.springpoems.infra.validation;

import org.springframework.stereotype.Component;

import com.api.springpoems.entities.User;
import com.api.springpoems.infra.exceptions.ValidationException;

@Component
public class UserValidator {
    public void checkActiveUser(User user) {
        if(user != null && !user.getActive()) {
            throw new ValidationException("User deactivated, try registering a new one.");
        }
    }
}
