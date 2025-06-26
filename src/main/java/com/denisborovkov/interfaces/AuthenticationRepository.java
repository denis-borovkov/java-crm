package com.denisborovkov.interfaces;

import java.util.Map;

public interface AuthenticationRepository {
    void save(Map<String, String> authMap);
}
