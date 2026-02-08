package com.micromouselab.mazes.repository;

import com.micromouselab.mazes.domain.Role;
import com.micromouselab.mazes.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findById(Long id);

    boolean existsById(Long id);

    List<User> findByRole(Role role);
}
