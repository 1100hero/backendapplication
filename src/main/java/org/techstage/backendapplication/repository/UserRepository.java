package org.techstage.backendapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.techstage.backendapplication.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@Param("email") String email);

    boolean existsByTelephone(@Param("telephone") String telephone);
}
