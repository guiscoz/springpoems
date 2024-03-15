package com.api.springpoems.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.springpoems.entities.Comment;
import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User registerUser(User user) {
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        User newUser = userRepository.save(user);
        return newUser;
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
