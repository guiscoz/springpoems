package com.api.springpoems.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;

public interface PoemRepository extends JpaRepository<Poem, Long>  {
    Page<Poem> findAllByAuthorAndActiveTrue(User author, Pageable pageable);
    Page<Poem> findAllByIdAndActiveTrue(Long id, Pageable pageable);

    List<Poem> findAllByAuthorAndActiveTrue(User author);

    Poem findByIdAndActiveTrue(Long id);
    Poem findByIdAndAuthorAndActiveTrue(Long id, User author);
}
