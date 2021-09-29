package com.example.crudapipractice.Controller;

import com.example.crudapipractice.Model.User;
import com.example.crudapipractice.Repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    UserRepository newUserRepository;

    public UserController(UserRepository newUserRepository) {
        this.newUserRepository = newUserRepository;
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return this.newUserRepository.findAll();
    }

    @PostMapping("/users")
    public User addUserToDatabase(@RequestBody User newUser) {
        return this.newUserRepository.save(newUser);
    }

    @GetMapping("/users/{userId}")
    public Optional<User> getUserById(@PathVariable Long userId) {
        return this.newUserRepository.findById(userId);
    }

    @PatchMapping("/users/{userId}")
    public User modifyUserById(@PathVariable Long userId, @RequestBody Map<String, Object> patchMap) {
        User oldUser = this.newUserRepository.findById(userId).get();
        patchMap.forEach((key, value) -> {
            switch (key) {
                case "email" -> oldUser.setEmail((String) value);
                case "password" -> oldUser.setPassword((String) value);
                case "authenticated" -> oldUser.setAuthenticated((Boolean) value);
            }
        });
        return this.newUserRepository.save(oldUser);
    }

    @DeleteMapping("/users/{userId}")
    public User deleteUserFromDatabase(@PathVariable Long userId) {
        User userToDelete = this.newUserRepository.findById(userId).get();
        this.newUserRepository.deleteById(userId);
        return userToDelete;
    }

    @PostMapping("/users/authenticate")
    public HashMap<String, Object> authenticateUser(@RequestBody User newUser) {
        HashMap<String, Object> outputMap = new HashMap<>();

        User userToUpdate = this.newUserRepository.findFirstByEmail(newUser.getEmail()).get();
        String storedPassword = userToUpdate.getPassword();
        String checkPassword = newUser.getPassword();

        if (storedPassword.equals(checkPassword)) {
            userToUpdate.setAuthenticated(true);
            this.newUserRepository.save(userToUpdate);
            outputMap.put("authenticated", true);
            outputMap.put("user", userToUpdate);
            return outputMap;
        } else {
            outputMap.put("authenticated", false);
        }
    return outputMap;
    }

}
