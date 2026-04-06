package com.denisborovkov.javacrm.repository;

import com.denisborovkov.javacrm.entity.RecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, Long> {
    Optional<RecoveryToken> findByToken(String token);
    List<RecoveryToken> findAllByEmail(String email);
}
