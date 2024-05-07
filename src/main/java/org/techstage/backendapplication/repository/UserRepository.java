package org.techstage.backendapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.techstage.backendapplication.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    Optional<User> findByNameOrEmail(String username, String email);

    boolean existsByEmail(@Param("email") String email);

    boolean existsByTelephone(@Param("telephone") String telephone);
}
