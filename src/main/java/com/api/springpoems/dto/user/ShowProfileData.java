package com.api.springpoems.dto.user;

import java.time.LocalDate;

import com.api.springpoems.entities.User;
import com.api.springpoems.enums.Gender;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record ShowProfileData(
    String username,
    String email,
    LocalDate memberSince,

    @Enumerated(EnumType.STRING)
    Gender gender,

    String firstName,
    String lastName,
    String description,
    LocalDate birthday
) {
    public ShowProfileData(User user) {
        this(
            user.getUsername(),
            user.getEmail(), 
            user.getMemberSince(), 
            user.getGender(), 
            user.getFirstName(), 
            user.getLastName(), 
            user.getDescription(), 
            user.getBirthday()
        );
    }
}
