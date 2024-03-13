package com.api.springpoems.dto.user;

import java.time.LocalDate;

import com.api.springpoems.enums.Gender;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;

public record UpdateProfileData(
    @Enumerated(EnumType.STRING)
    Gender gender,

    @Size(max = 255, message = "Your first name can't have more than 255 characters.")
    String firstName,

    @Size(max = 255, message = "Your last name can't have more than 255 characters.")
    String lastName,

    @Size(max = 255, message = "Your description can't have more than 255 characters.")
    String description,
    LocalDate birthday
) {}
