package com.denisborovkov.interfaces;

public interface UserRepository extends CrudRepository<UserDetails, Long> {
    UserDetails findByUsername(String username);
}
