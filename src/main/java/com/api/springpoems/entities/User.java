package com.api.springpoems.entities;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.api.springpoems.dto.user.RegisterUserData;
import com.api.springpoems.dto.user.UpdateProfileData;
import com.api.springpoems.enums.Gender;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private LocalDate memberSince;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String firstName;
    private String lastName;
    private String description;
    private LocalDate birthday;
    private Boolean active;

    public User(RegisterUserData data) {
        this.username = data.username();
        this.password = data.password();
        this.email = data.email();
        this.memberSince = LocalDate.now();
        this.active = true;
    }

    public void updateProfile(@Valid UpdateProfileData data) {
        if(data.gender() != null) {
            this.gender = data.gender();
        }

        if(data.firstName() != null) {
            this.firstName = data.firstName();
        }

        if(data.lastName() != null) {
            this.lastName = data.lastName();
        }

        if(data.description() != null) {
            this.description = data.description();
        }

        if(data.birthday() != null) {
            this.birthday = data.birthday();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
