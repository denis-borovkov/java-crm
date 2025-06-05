package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.MessageRepository;
import com.denisborovkov.interfaces.UserDetails;

import java.util.HashMap;
import java.util.Map;

public class MessageRepo implements MessageRepository {
    private final Map<String, UserDetails> messagesData = new HashMap<>();
}
