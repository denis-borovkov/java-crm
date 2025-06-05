package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.AuthenticationRepository;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationRepo implements AuthenticationRepository {
    private final Map<String, String> authData = new HashMap<>();

}
