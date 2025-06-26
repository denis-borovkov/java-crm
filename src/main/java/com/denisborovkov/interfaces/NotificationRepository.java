package com.denisborovkov.interfaces;

import com.denisborovkov.models.Notification;

public interface NotificationRepository {
    void save(Notification notification);
}
