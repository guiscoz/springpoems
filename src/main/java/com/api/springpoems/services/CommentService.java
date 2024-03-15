package com.api.springpoems.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.springpoems.dto.comment.CreateCommentData;
import com.api.springpoems.dto.comment.ListCommentsData;
import com.api.springpoems.dto.comment.SendCommentData;
import com.api.springpoems.dto.comment.ShowCommentData;
import com.api.springpoems.entities.Comment;
import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;
import com.api.springpoems.repositories.CommentRepository;
import com.api.springpoems.repositories.PoemRepository;
import com.api.springpoems.repositories.UserRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PoemRepository poemRepository;

    @Autowired
    private UserRepository userRepository;

    public ShowCommentData getComment(Long id) {
        Comment comment = commentRepository.findByIdAndActiveTrue(id);
        ShowCommentData data = new ShowCommentData(comment);
        return data;
    }

    public Page<ListCommentsData> getUserComments(User author, Pageable pagination) {
        var comments = commentRepository.findAllByAuthorAndActiveTrue(author, pagination).map(ListCommentsData::new);
        return comments;
    }

    public Page<ListCommentsData> getPoemComments(Long id, Pageable pagination) {
        Poem poem = poemRepository.findByIdAndActiveTrue(id);
        var comments = commentRepository.findAllByPoemAndActiveTrue(poem, pagination).map(ListCommentsData::new);
        return comments;
    }

    public void create(SendCommentData data, User user, Long id) {
        Poem poem = poemRepository.findByIdAndActiveTrue(id);
        CreateCommentData updatedData = new CreateCommentData(data.content(), user, poem);
        Comment comment = new Comment(updatedData);
        commentRepository.save(comment);
    }

    public void update(User author, Long id, SendCommentData data) {
        Comment comment = commentRepository.findByIdAndAuthorAndActiveTrue(id, author);
        comment.updateComment(data);
        commentRepository.save(comment);
    }

    public void delete(Long id, User author) {
        Comment comment = commentRepository.findByIdAndAuthorAndActiveTrue(id, author);
        comment.setActive(false);
        commentRepository.save(comment);
    }
}
