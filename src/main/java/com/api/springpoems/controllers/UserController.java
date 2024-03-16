package com.api.springpoems.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.springpoems.dto.user.ChangeEmailData;
import com.api.springpoems.dto.user.ChangePasswordData;
import com.api.springpoems.dto.user.LoginData;
import com.api.springpoems.dto.user.RegisterUserData;
import com.api.springpoems.dto.user.ShowProfileData;
import com.api.springpoems.dto.user.UpdateProfileData;
import com.api.springpoems.entities.User;
import com.api.springpoems.infra.exceptions.ValidationException;
import com.api.springpoems.infra.security.TokenDataJWT;
import com.api.springpoems.infra.security.TokenService;
import com.api.springpoems.infra.validation.UserValidator;
import com.api.springpoems.repositories.UserRepository;
import com.api.springpoems.services.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserValidator validator;

    private void passwordConfirmation(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new ValidationException("The password and the it's confirmation do not match.");
        }
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity register(@ModelAttribute @RequestBody @Valid RegisterUserData data) {
        if (repository.findByUsername(data.username()) != null) {
            throw new IllegalArgumentException("Username already exists!");
        }

        passwordConfirmation(data.password(), data.confirmPassword());
        var user = new User(data);
        System.err.println(user);
        User newUser = userService.registerUser(user);
        String tokenJWT = tokenService.generateToken(newUser);

        return ResponseEntity.ok(new TokenDataJWT(tokenJWT));
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity login(@ModelAttribute @RequestBody @Valid LoginData data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var authentication = manager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenDataJWT(tokenJWT));
    }

    @GetMapping("/profile")
    public ResponseEntity yourProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);

        ShowProfileData showProfileData = new ShowProfileData(
            user.getUsername(),
            user.getEmail(),
            user.getMemberSince(),
            user.getGender(),
            user.getFirstName(),
            user.getLastName(),
            user.getDescription(),
            user.getBirthday()
        );

        return ResponseEntity.ok(showProfileData);
    }
    

    @PutMapping("/profile/change_password")
    @Transactional
    public ResponseEntity changePassword(@ModelAttribute @RequestBody @Valid ChangePasswordData data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);

        if (!passwordEncoder.matches(data.currentPassword(), user.getPassword())) {
            throw new ValidationException("The current password is incorrect");
        }

        passwordConfirmation(data.newPassword(), data.confirmPassword());
        String encodedNewPassword = new BCryptPasswordEncoder().encode(data.newPassword());
        user.setPassword(encodedNewPassword);
        repository.save(user);
        
        return ResponseEntity.ok("Password changed successfully!");
    }

    @PutMapping("/profile/change_email")
    @Transactional
    public ResponseEntity changeEmail(@ModelAttribute @RequestBody @Valid ChangeEmailData data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);

        if(data.email().equals(user.getEmail())) {
            throw new ValidationException("You're already using this email.");
        }

        user.setEmail(data.email());
        repository.save(user);
        
        return ResponseEntity.ok("Email changed successfully!");
    }

    @PutMapping("/profile/update")
    @Transactional
    public ResponseEntity updateProfile(@ModelAttribute @RequestBody @Valid UpdateProfileData data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);

        user.updateProfile(data);
        repository.save(user);
        
        return ResponseEntity.ok(new ShowProfileData(user));
    }

    @GetMapping("/{username}")
    public ResponseEntity anotherProfile(@PathVariable String username) {
        User user = repository.findByUsernameAndActiveTrue(username);

        ShowProfileData showProfileData = new ShowProfileData(
            user.getUsername(),
            user.getEmail(),
            user.getMemberSince(),
            user.getGender(),
            user.getFirstName(),
            user.getLastName(),
            user.getDescription(),
            user.getBirthday()
        );

        return ResponseEntity.ok(showProfileData);
    }

    @GetMapping("/user-list")
    public ResponseEntity<Page<ShowProfileData>> userList(@PageableDefault(size=10, sort = {"username"}) Pageable pagination) {
        var page = repository.findAllByActiveTrue(pagination).map(ShowProfileData::new);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/profile/delete")
    @Transactional
    public ResponseEntity deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);
        userService.deleteUser(user);
        
        return ResponseEntity.ok("User removed successfully!");
    }
}
