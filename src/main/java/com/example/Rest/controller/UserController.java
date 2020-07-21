package com.example.Rest.controller;

import com.example.Rest.model.User;
import com.example.Rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Level;

import static com.example.Rest.RestApplication.LOGGER;

@RestController
public class UserController {

    private final UserService userService;

    /**
     * Initialize userService with UserServiceImplementation bean
     *
     * @param userService - instance of UserService, realizes CRUD methods
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Processes requests to displaying users
     *
     * @return Http Status - OK and users database or NOT_FOUND if database empty
     */
    @GetMapping(value = "/users")
    public ResponseEntity<Map<Integer, User>> read() {
        LOGGER.log(Level.INFO, "Received a request to output users");
        final Map<Integer, User> users = userService.read();
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Processes requests to add a user
     *
     * @param user - User`s info from request
     * @return Http Status - CREATED
     */
    @PostMapping(value = "/users")
    public ResponseEntity<?> create(@RequestBody User user) {
        LOGGER.log(Level.INFO, "Received a request to create a user");
        final boolean created = userService.create(user);
        return created
                ? new ResponseEntity<>(user, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Processes requests to show user by ID
     *
     * @param id - User`s id from path
     * @return Http Status - OK or NOT_FOUND
     */
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<User> readUserById(@PathVariable(name = "id") int id) {
        LOGGER.log(Level.INFO, "Received a request to get a user by ID");
        final User user = userService.readUserById(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Processes requests to update user by ID
     *
     * @param id   - User`s id from path
     * @param user - User`s info from request
     * @return Http Status - OK or NOT_FOUND
     */
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody User user) {
        LOGGER.log(Level.INFO, "Received a request to update user information");
        final boolean updated = userService.update(id, user);
        return updated
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Processes requests to delete user by ID
     *
     * @param id - User`s id from path
     * @return Http Status - OK or NOT_MODIFIED
     */
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        LOGGER.log(Level.INFO, "Received a request to delete a user by ID");
        final boolean deleted = userService.delete(id);
        return deleted
                ? new ResponseEntity<>((HttpStatus.OK))
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
