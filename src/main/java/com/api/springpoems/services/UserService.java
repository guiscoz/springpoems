package com.api.springpoems.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.springpoems.dto.user.ChangeEmailData;
import com.api.springpoems.dto.user.ChangePasswordData;
import com.api.springpoems.dto.user.RegisterUserData;
import com.api.springpoems.dto.user.ShowProfileData;
import com.api.springpoems.entities.Comment;
import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;
import com.api.springpoems.infra.exceptions.ValidationException;
import com.api.springpoems.repositories.CommentRepository;
import com.api.springpoems.repositories.PoemRepository;
import com.api.springpoems.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PoemRepository poemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void passwordConfirmation(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new ValidationException("The password and the it's confirmation do not match.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User registerUser(RegisterUserData data) {
        if (userRepository.findByUsername(data.username()) != null) {
            throw new IllegalArgumentException("Username already exists!");
        }

        passwordConfirmation(data.password(), data.confirmPassword());
        var user = new User(data);

        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        User newUser = userRepository.save(user);
        return newUser;
    }

    public void changePassword(ChangePasswordData data, User user) {
        if (!passwordEncoder.matches(data.currentPassword(), user.getPassword())) {
            throw new ValidationException("The current password is incorrect");
        }

        passwordConfirmation(data.newPassword(), data.confirmPassword());
        String encodedNewPassword = new BCryptPasswordEncoder().encode(data.newPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }

    public void changeEmail(ChangeEmailData data, User user) {
        if(data.email().equals(user.getEmail())) {
            throw new ValidationException("You're already using this email.");
        }

        user.setEmail(data.email());
        userRepository.save(user);
    }

    public ShowProfileData getProfileData(String username, User authenticatedUser) {
        if (authenticatedUser != null) {
            return new ShowProfileData(
                authenticatedUser.getUsername(),
                authenticatedUser.getEmail(),
                authenticatedUser.getMemberSince(),
                authenticatedUser.getGender(),
                authenticatedUser.getFirstName(),
                authenticatedUser.getLastName(),
                authenticatedUser.getDescription(),
                authenticatedUser.getBirthday()
            );
        } else if (username != null) {
            User user = userRepository.findByUsernameAndActiveTrue(username);
            if (user != null) {
                return new ShowProfileData(
                    user.getUsername(),
                    user.getEmail(),
                    user.getMemberSince(),
                    user.getGender(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getDescription(),
                    user.getBirthday()
                );
            } else {
                throw new ValidationException("No user found with this username: " + username);
            }
        } else {
            throw new IllegalArgumentException("No authenticated user or username provided.");
        }
    }

    public Page<ShowProfileData> userList(Pageable pagination) {
        return userRepository.findAllByActiveTrue(pagination).map(ShowProfileData::new);
    }

    public void deleteUser(User user) {
        user.setActive(false);
        userRepository.save(user);

        List<Poem> poems = poemRepository.findAllByAuthorAndActiveTrue(user);
        for (Poem poem : poems) {
            poem.setActive(false);
        }

        poemRepository.saveAll(poems);

        List<Comment> comments = commentRepository.findAllByAuthorAndActiveTrue(user);
        for (Comment comment : comments) {
            comment.setActive(false);
        }

        commentRepository.saveAll(comments);
    }
}
