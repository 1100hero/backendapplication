package org.techstage.backendapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.techstage.backendapplication.model.user.User;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(@Param("username") String username);
    Optional<User> findOneByEmail(@Param("email") String email);
    Optional<User> findByEmail(@Param("email") String email);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.enabled = true")
    boolean checkIfEnabled(@Param("email") String email);
    boolean existsByEmail(@Param("email") String email);
    boolean existsByTelephone(@Param("telephone") String telephone);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.enabled = TRUE WHERE u.email = ?1 AND u.confirmedToken = ?2")
    void enableUser(String email, String token);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.confirmedToken = ?2 WHERE u.id = ?1")
    void updateConfirmedTokenById(Long id, String newToken);
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.enabled = false")
    void deleteAllByEnabledFalse();
    int countUsersByEmail(String email);
    Optional<User> findUserById(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = ?1")
    void deleteUserById(Long id);
}
