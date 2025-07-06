package com.denisborovkov.interfaces;

import java.util.Map;

public interface UserRepository extends CrudRepository<UserDetails, Long> {
    Map<Long, UserDetails> getUserDatabase();
    void loadAll(Map<Long, UserDetails> users);
    UserDetails findByUsername(String username);
    UserDetails findByEmail(String email);
}
