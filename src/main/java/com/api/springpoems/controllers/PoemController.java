package com.api.springpoems.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.springpoems.dto.poem.SendPoemData;
import com.api.springpoems.dto.poem.ShowPoemData;
import com.api.springpoems.entities.User;
import com.api.springpoems.infra.exceptions.ValidationException;
import com.api.springpoems.services.PoemService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@RestController
public class PoemController {
    @Autowired
    PoemService service;
    
    @GetMapping("/{username}/poem/{id}") 
    public ResponseEntity getPoem(@PathVariable String username, @PathVariable Long id) {
        ShowPoemData poem = service.getPoem(id);
        return ResponseEntity.ok(poem);
    }

    @GetMapping("/{username}/poems") 
    public ResponseEntity userPoems(@PathVariable String username, @PageableDefault(size=10, sort = {"title"}) Pageable pagination) {
        var page = service.getAuthorPoems(username, pagination);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/profile/poems") 
    public ResponseEntity yourPoems(@PageableDefault(size=10, sort = {"title"}) Pageable pagination) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        var page = service.getAuthorPoems(user.getUsername(), pagination);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/profile/new_poem")
    @Transactional
    public ResponseEntity create(@ModelAttribute @RequestBody @Valid SendPoemData data) {
        if(data.title().isEmpty() || data.content().isEmpty()) {
            throw new ValidationException("Your poem needs content and a title.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        service.create(data, user);
        return ResponseEntity.ok(data);
    }

    @PutMapping("/profile/update_poem/{id}")
    @Transactional
    public ResponseEntity update(@ModelAttribute @RequestBody @Valid SendPoemData data, @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        service.update(user, id, data);
        return ResponseEntity.ok(data);
    }
}
