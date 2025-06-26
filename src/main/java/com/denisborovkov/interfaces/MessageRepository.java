package com.denisborovkov.interfaces;

import com.denisborovkov.models.Message;

public interface MessageRepository {
    void saveMessage(UserDetails user, Message message);
}
