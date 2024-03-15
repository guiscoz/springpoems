package com.api.springpoems.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.springpoems.entities.Comment;
import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;

public interface CommentRepository extends JpaRepository<Comment, Long>  {
    Page<Comment> findAllByAuthorAndActiveTrue(User author, Pageable pageable);
    Page<Comment> findAllByPoemAndActiveTrue(Poem poem, Pageable pageable);
    Page<Comment> findAllByIdAndActiveTrue(Long id, Pageable pageable);

    List<Comment> findAllByAuthorAndActiveTrue(User author);
    List<Comment> findAllByPoemAndActiveTrue(Poem poem);
    
    Comment findByIdAndActiveTrue(Long id);
    Comment findByIdAndAuthorAndActiveTrue(Long id, User author);
    Comment findByIdAndPoemAndActiveTrue(Long id, Poem poem);
}
