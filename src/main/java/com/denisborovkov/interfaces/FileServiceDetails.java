package com.denisborovkov.interfaces;

public interface FileServiceDetails {
    void saveUsersToFile();

    void loadUsersFromFile();

    void saveMessagesToFile();

    void loadMessagesFromFile();

    void saveNotificationsToFile();

    void loadNotificationsFromFile();

    void saveAuthDataToFile();

    void loadAuthDataFromFile();
}
