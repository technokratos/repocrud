package org.repocrud.repository;

import java.util.UUID;
import org.repocrud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    User findByUsernameIgnoreCase(String username);

    boolean existsByEmail(String email);
}
