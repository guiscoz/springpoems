package com.api.springpoems.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.springpoems.dto.poem.CreatePoemData;
import com.api.springpoems.dto.poem.ListAuthorPoemsData;
import com.api.springpoems.dto.poem.SendPoemData;
import com.api.springpoems.dto.poem.ShowPoemData;
import com.api.springpoems.entities.Comment;
import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;
import com.api.springpoems.repositories.CommentRepository;
import com.api.springpoems.repositories.PoemRepository;
import com.api.springpoems.repositories.UserRepository;

@Service
public class PoemService {
    @Autowired
    private PoemRepository poemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    public ShowPoemData getPoem(Long id) {
        Poem poem = poemRepository.findByIdAndActiveTrue(id);
        ShowPoemData data = new ShowPoemData(poem);
        return data;
    }

    public Page<ListAuthorPoemsData> getAuthorPoems(String username, Pageable pagination) {
        User author = userRepository.findByUsernameAndActiveTrue(username);
        var poems = poemRepository.findAllByAuthorAndActiveTrue(author, pagination).map(ListAuthorPoemsData::new);
        return poems;
    }

    public void create(SendPoemData data, User author) {
        CreatePoemData updatedData = new CreatePoemData(data.title(), data.content(), author);
        Poem poem = new Poem(updatedData);
        poemRepository.save(poem);
    }

    public void update(User author, Long id, SendPoemData data) {
        Poem poem = poemRepository.findByIdAndAuthorAndActiveTrue(id, author);
        poem.updatePoem(data);
        poemRepository.save(poem);
    }

    public void delete(Long id, User author) {
        Poem poem = poemRepository.findByIdAndAuthorAndActiveTrue(id, author);
        poem.setActive(false);
        poemRepository.save(poem);

        List<Comment> comments = commentRepository.findAllByPoemAndActiveTrue(poem);
        for (Comment comment : comments) {
            comment.setActive(false);
        }
        commentRepository.saveAll(comments);
    }
}
