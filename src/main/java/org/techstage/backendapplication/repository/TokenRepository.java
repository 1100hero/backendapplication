package org.techstage.backendapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.techstage.backendapplication.model.token.Token;
import org.techstage.backendapplication.model.user.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE Token c SET c.confirmedAt = ?2 WHERE c.token = ?1")
    void updateConfirmedAt(String token, LocalDateTime confirmedAt);

    @Query("SELECT t FROM Token t WHERE t.user.email = :email AND t.confirmedAt IS NOT NULL")
    Optional<Token> findTokenByUserEmailIfConfirmed(@Param("email") String email);

    @Query("SELECT t.id FROM Token t WHERE t.token = ?1")
    Optional<Integer> findUserIdByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token t WHERE t.expiresAt < :now")
    void deleteByExpiredAtBefore(Date now);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token t WHERE t.user.id <> :id")
    void deleteTokenExcept(@Param("id") Integer id);

    @Query("SELECT DISTINCT t.user.id FROM Token t WHERE t.expiresAt < :now")
    Optional<List<Integer>> findUserIdsWithExpiredTokens(Date now);
}
