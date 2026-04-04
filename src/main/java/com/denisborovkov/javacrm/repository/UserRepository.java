package com.denisborovkov.javacrm.repository;

import com.denisborovkov.javacrm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUsersByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
