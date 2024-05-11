package org.techstage.backendapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.techstage.backendapplication.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
