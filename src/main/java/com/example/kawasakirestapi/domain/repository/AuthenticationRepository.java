package com.example.kawasakirestapi.domain.repository;

import com.example.kawasakirestapi.infrastructure.entity.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Authenticationのリポジトリ
 *
 * @author kawasakiryosuke
 */
@Repository
public interface AuthenticationRepository extends JpaRepository<AuthenticationToken, Long> {

    Optional<AuthenticationToken> findByUserId(Long userId);

    Optional<AuthenticationToken> findByAuthToken(String authToken);
}
