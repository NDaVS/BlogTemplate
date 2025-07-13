package ru.happines.springbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.happines.springbackend.dto.CreateUserDTO;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping
    public List<String> getUsersFullNames() {
        return userService.findAllNames();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public User createUser(@RequestBody CreateUserDTO userDTO) {
        return userService.create(userDTO);
    }
}
