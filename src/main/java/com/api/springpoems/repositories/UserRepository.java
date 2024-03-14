package com.api.springpoems.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.api.springpoems.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByUsername (String username);
    User findByUsernameAndActiveTrue (String username);
    Page<User> findAllByActiveTrue(Pageable pageable);
}
