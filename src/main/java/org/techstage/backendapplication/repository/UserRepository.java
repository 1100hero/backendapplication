package org.techstage.backendapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.techstage.backendapplication.model.token.Token;
import org.techstage.backendapplication.model.user.User;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(@Param("username") String username);

    Optional<User> findUserByName(String name);

    Optional<User> findUserBySurname(String surname);

    Optional<User> findUserByTelephone(String telephone);

    Optional<User> findOneByEmail(@Param("email") String email);

    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.enabled = true")
    boolean checkIfEnabled(@Param("email") String email);

    boolean existsByEmail(@Param("email") String email);

    boolean existsByTelephone(@Param("telephone") String telephone);

    Optional<User> findUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.enabled = TRUE WHERE u.email = ?1 AND u.confirmedToken = ?2")
    void enableUser(String email, String token);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.confirmedToken = ?2 WHERE u.id = ?1")
    void updateConfirmedTokenById(Integer id, String newToken);

    Optional<User> findUserByConfirmedToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.enabled = false")
    void deleteAllByEnabledFalse();

    int countUsersByEmail(String email);

    Optional<User> findUserById(Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteUserById(@Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token t WHERE t.user.id = :userId")
    void deleteTokensByUserId(@Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    void updateUserByName(@Param("id") Integer id, @Param("name") String name);

    @Query("SELECT u.id FROM User u WHERE u.confirmedToken = :token")
    Optional<Integer> findUserIdByToken(@Param("token") String token);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.surname = :surname WHERE u.id = :id")
    void updateUserBySurname(@Param("id") Integer id, @Param("surname") String surname);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.telephone = :telephone WHERE u.id = :id")
    void updateUserByTelephone(@Param("id") Integer id, @Param("telephone") String telephone);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updateUserPasswordByEmail(@Param("email") String email, @Param("password") String password);
}
