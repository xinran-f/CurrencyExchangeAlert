package org.sandyf.currencyexchangealert.controller;

import org.sandyf.currencyexchangealert.dao.UserDao;
import org.sandyf.currencyexchangealert.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // 1) save the new user (user.id will be auto‐generated)
        userDao.save(user);

        // 2) fetch it back so we have its generated ID & createdAt
        User created = userDao.findByUsername(user.getUsername())
                .orElseThrow();

        // 3) return 201 Created with Location header
        return ResponseEntity
                .created(URI.create("/api/users/" + created.getId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        return userDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> listUsers() {
        return ResponseEntity.ok(userDao.findAll());
    }
}