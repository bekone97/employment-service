package com.godeltech.mastery.securityservice.model.repository;

import com.godeltech.mastery.securityservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);
}
