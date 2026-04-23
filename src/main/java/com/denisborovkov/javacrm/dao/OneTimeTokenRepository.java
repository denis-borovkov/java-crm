package com.denisborovkov.javacrm.dao;

import com.denisborovkov.javacrm.entity.OTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimeTokenRepository extends JpaRepository<OTToken, String> {
    void deleteAllByEmail(String email);
    Optional<OTToken> findTopByEmailOrderByIssuedAtDesc(String email);
}
