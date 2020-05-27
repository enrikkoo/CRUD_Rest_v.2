package com.example.Rest.service;

import com.example.Rest.model.User;

import java.util.Map;

/**
 * Here are the basic CRUD methods that are implemented in UserServiceImplementation
 * @see UserServiceImplementation
 */
public interface UserService {

    void create (User user);

    Map<Integer, User> read();

    User readUserById(int id);

    boolean update(User user, int id);

    boolean delete(int id);
}
