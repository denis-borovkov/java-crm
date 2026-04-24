package com.denisborovkov.javacrm.auth.token;

import com.denisborovkov.javacrm.auth.token.OTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimeTokenRepository extends JpaRepository<OTToken, String> {
    void deleteAllByEmail(String email);
    Optional<OTToken> findTopByEmailOrderByIssuedAtDesc(String email);
}



