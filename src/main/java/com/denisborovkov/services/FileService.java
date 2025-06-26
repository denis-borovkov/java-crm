package com.denisborovkov.services;

import com.denisborovkov.ConsoleUI;
import com.denisborovkov.interfaces.*;
import com.denisborovkov.models.Message;
import com.denisborovkov.models.Notification;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class FileService implements FileServiceDetails {
    private final File storageFile = new File("/resources/users.json");
    private final File ordersFile = new File("/resources/orders.json");
    private final File clientsFile = new File("/resources/clients.json");
    private final File messagesFile = new File("/resources/messages.json");
    private final File notificationsFile = new File("/resources/notifications.json");
    private final File authenticationDataFile = new File("/resources/authdata.json");

    private final UserRepository userRepo;
    private final OrderRepository orderRepo;
    private final ClientRepository clientRepo;
    private final NotificationRepository notificationRepo;
    private final MessageRepository messageRepo;
    private final AuthenticationRepository authenticationRepo;
    private final ConsoleUI ui;
    private final ObjectMapper objectMapper;

    public FileService(UserRepository userRepo,
                       OrderRepository orderRepo,
                       ClientRepository clientRepo,
                       NotificationRepository notificationRepo,
                       MessageRepository messageRepo,
                       AuthenticationRepository authenticationRepo,
                       ConsoleUI ui) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.clientRepo = clientRepo;
        this.notificationRepo = notificationRepo;
        this.messageRepo = messageRepo;
        this.authenticationRepo = authenticationRepo;
        this.ui = ui;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void saveUsersToFile() {
        UserDetails users = userRepo.getAll();
        try {
            objectMapper.writeValue(storageFile, userRepo.save(users));
        } catch (IOException e) {
            ui.printError("Не удалось сохранить пользователей " + e.fillInStackTrace());
        }
    }

    @Override
    public void loadUsersFromFile() {
        try {
            Map<String, UserDetails> loadedUsers = objectMapper.readValue(storageFile, new TypeReference<>() {});
            userRepo.save((UserDetails) loadedUsers.values());
        } catch (IOException e) {
            ui.printError("Не удалось загрузить пользователей. Будет создан новый файл. \n" + e.getMessage());
        }
    }

    @Override
    public void saveMessagesToFile() {
        try {
            objectMapper.writeValue(messagesFile, messageRepo);
        } catch (IOException e) {
            ui.printError("Ошибка сохранения файла сообщений\n");
        }
    }

    @Override
    public void loadMessagesFromFile() {
        try {
            Map<String, List<Message>> loadedMessages = objectMapper.readValue(messagesFile, new TypeReference<>() {});
            messageRepo.save(loadedMessages);
        } catch (IOException e) {
            ui.printError("Ошибка загрузки файла сообщений. Будет создан новый файл\n");
        }
    }

    @Override
    public void saveNotificationsToFile() {
        try {
            objectMapper.writeValue(notificationsFile, notificationRepo.);
        } catch (IOException e) {
            ui.printError("Ошибка сохранения уведомлений: " + e.getMessage());
        }
    }

    @Override
    public void loadNotificationsFromFile() {
        if (!notificationsFile.exists()) {
            ui.printError("Файл уведомлений не найден, создаётся новый.");
            return;
        }
        try {
            Map<String, Queue<Notification>> loadedNotifications = objectMapper.readValue(notificationsFile, new TypeReference<>() {});
            notificationRepo.putAll(loadedNotifications);
        } catch (IOException e) {
            ui.printError("Ошибка загрузки уведомлений: " + e.getMessage());
        }
    }
    @Override
    public void saveAuthDataToFile() {
        try {
            objectMapper.writeValue(authenticationDataFile, authenticationRepo.);
        } catch (IOException e) {
            ui.printError("Не удалось сохранить пользователей " + e.getMessage());
        }
    }

    @Override
    public void loadAuthDataFromFile() {
        if (!authenticationDataFile.exists()) {
            ui.printError("Файл пользовательских ключей не найден, создаётся новый.");
            return;
        }
        try {
            Map<String, String> loadedAuthData = objectMapper.readValue(authenticationDataFile, new TypeReference<>() {});
            authenticationRepo.putAll(loadedAuthData);
        } catch (IOException e) {
            ui.printError("Не удалось загрузить пользователей. Будет создан новый файл. \n" + e.getMessage());
        }
    }
}