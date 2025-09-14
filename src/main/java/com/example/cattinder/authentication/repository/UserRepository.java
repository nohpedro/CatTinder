package com.example.cattinder.authentication.repository;

import com.example.cattinder.authentication.model.AuthUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class UserRepository {
    private final ConcurrentMap<String, AuthUser> users = new ConcurrentHashMap<>();

    public Optional<AuthUser> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

    public AuthUser save(AuthUser user) {
        users.put(user.getEmail(), user);
        return user;
    }

    public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }

    public void deleteByEmail(String email) {
        users.remove(email);
    }
}