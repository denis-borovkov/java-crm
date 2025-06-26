package com.denisborovkov.repositories;

import com.denisborovkov.interfaces.NotificationRepository;
import com.denisborovkov.models.Notification;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class NotificationRepo implements NotificationRepository {
    private final Map<String, Queue<Notification>> notificationQueue = new HashMap<>();

    public void save(Notification notification) {
        System.out.println("TODO" + notification);
    }
}
