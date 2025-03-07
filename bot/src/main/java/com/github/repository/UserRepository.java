package com.github.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private final List<String> userList = new ArrayList<>();

    public String addUser(String chatId) {
        if (userList.contains(chatId)) {
            return "ALREADY_EXIST";
        }

        userList.add(chatId);
        return "ADDED";
    }
}
