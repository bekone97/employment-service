package com.godeltech.mastery.securityservice.service;


import com.godeltech.mastery.securityservice.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();

    User getById(Long id);

    void deleteById(Long id);

    User save(User user);

    User getUserByUsername(String username);
}
