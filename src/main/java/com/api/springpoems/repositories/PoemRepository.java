package com.api.springpoems.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;

public interface PoemRepository  extends JpaRepository<Poem, Long>  {
    Page<Poem> findAllByAuthorAndActiveTrue(User author, Pageable pageable);
    Poem findByIdAndActiveTrue(Long id);
    Poem findByIdAndAuthor(Long id, User author);
}
