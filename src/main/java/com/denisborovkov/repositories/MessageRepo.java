package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.MessageRepository;
import com.denisborovkov.interfaces.UserDetails;
import com.denisborovkov.models.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageRepo implements MessageRepository {
    private final Map<String, ArrayList<Message>> messagesData = new HashMap<>();

    public void saveMessage(UserDetails username, Message message) {

        messagesData.getOrDefault(username.getName(), new ArrayList<String>().add(message));
    }
}
