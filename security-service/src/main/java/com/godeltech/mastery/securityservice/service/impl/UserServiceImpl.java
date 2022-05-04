package com.godeltech.mastery.securityservice.service.impl;

import com.godeltech.mastery.securityservice.exception.NotUniqueResourceException;
import com.godeltech.mastery.securityservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.securityservice.model.entity.User;
import com.godeltech.mastery.securityservice.model.repository.UserRepository;
import com.godeltech.mastery.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public List<User> findAll() {
        log.debug("Getting all users ");
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        log.debug("Getting user by id:{}",id);
        return userRepository.findById(id)
                .orElseThrow(()->{
                    log.error("User wasn't found by id :{}",id);
                   return new ResourceNotFoundException(User.class,"username",id);
                });
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting user by id:{}",id);
        var user =getById(id);
        userRepository.deleteById(user.getId());
    }

    @Override
    public User save(User user) {
        if(userRepository.existsUserByUsername(user.getUsername()))
            throw new NotUniqueResourceException(User.class,"username",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()->{
                    log.error("User not found by username:{}",username);
                    return new ResourceNotFoundException(User.class,"username",username);
                });
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Getting user by username:{}", username);
        var user = getUserByUsername(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }
}
