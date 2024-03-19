package com.api.springpoems.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.springpoems.dto.comment.SendCommentData;
import com.api.springpoems.dto.comment.ShowCommentData;
import com.api.springpoems.entities.User;
import com.api.springpoems.infra.validation.UserValidator;
import com.api.springpoems.services.CommentService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
public class CommentController {
    @Autowired
    CommentService service;

    @Autowired
    private UserValidator validator;
    
    @GetMapping("/comment/{id}") 
    public ResponseEntity getPoem(
        @PathVariable Long id
    ) {
        ShowCommentData comment = service.getComment(id);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/poem/{id}/comments") 
    public ResponseEntity poemComments(
        @PathVariable Long id,  
        @PageableDefault(size=10, sort = {"lastUpdate"}) Pageable pagination
    ) {
        var page = service.getPoemComments(id, pagination);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/profile/comments") 
    public ResponseEntity userComments(@PageableDefault(size=10, sort = {"lastUpdate"}) Pageable pagination) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);

        var page = service.getUserComments(user, pagination);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/poem/{id}/new_comment")
    @Transactional
    public ResponseEntity create(
        @ModelAttribute @RequestBody @Valid SendCommentData data,
        @PathVariable Long id
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);

        service.create(data, user, id);
        return ResponseEntity.ok(data);
    }

    @PutMapping("/comment/{id}")
    @Transactional
    public ResponseEntity update(@ModelAttribute @RequestBody @Valid SendCommentData data, @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);

        service.update(user, id, data);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        validator.checkActiveUser(user);

        service.delete(id, user);
        return ResponseEntity.ok("Comment removed successfully");
    }
}
