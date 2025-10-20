package com.example.configms.model;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPrefsRepository {
  private final Map<String, UserPrefs> store = new ConcurrentHashMap<>();

  public UserPrefs save(UserPrefs p){
    if (p.getUpdatedAt()==null) p.setUpdatedAt(Instant.now());
    store.put(p.getUserId(), p);
    return p;
  }

  public UserPrefs find(String userId){
    return store.getOrDefault(
        userId,
        new UserPrefs(userId, false, 
                              true, 
                              true, 
                              true, Instant.now())
    );
  }
   public boolean exists(String userId) {
    return store.containsKey(userId);
  }
}