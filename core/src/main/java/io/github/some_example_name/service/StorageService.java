package io.github.some_example_name.service;

import io.github.some_example_name.model.User;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class StorageService {
    private static final String USER_FILE = "users.json";
    private Map<String, User> users;

    public StorageService() {
        users = new HashMap<>();
    }

    public void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(USER_FILE)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, User>>() {}.getType();
            Map<String, User> loaded = gson.fromJson(reader, type);
            if (loaded != null) users = loaded;
        } catch (IOException e) {
            System.out.println("Error loading users");
        }
    }

    public void saveUsers() {
        try (Writer writer = new FileWriter(USER_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("Error saving users");
        }
    }

    public boolean addUser(User user) {
        if (users.containsKey(user.getUsername())) return false;
        users.put(user.getUsername(), user);
        return true;
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public Map<String, User> getAllUsers() {
        return users;
    }
}
