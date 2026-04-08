package com.denisborovkov.javacrm.repository;

import com.denisborovkov.javacrm.entity.RecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimeTokenRepository extends JpaRepository<RecoveryToken, String> {
    void deleteAllByEmail(String email);
    Optional<RecoveryToken> findTopByEmailOrderByIssuedAtDesc(String email);
}
