package org.techstage.backendapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.techstage.backendapplication.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(@Param("username") String username);
    Optional<User> findOneByEmail(@Param("email") String email);
    String findUsernameByEmail(@Param("email") String email);
    Optional<User> findByEmail(@Param("email") String email);
    boolean existsByEmail(@Param("email") String email);
    boolean existsByTelephone(@Param("telephone") String telephone);
}
