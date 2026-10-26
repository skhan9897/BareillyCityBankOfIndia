package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.Notification;

import java.util.List;

public interface NotificationDao {
    long createNotification(Notification notification);
    List<Notification> findByUserId(long userId, int limit);
    void markAsRead(long notificationId);
}
