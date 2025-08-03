package ru.happines.springbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.happines.springbackend.dto.request.auth.ResetPasswordDTO;
import ru.happines.springbackend.exception.ServiceException;
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

    @PostMapping("password/reset")
    public ResponseEntity<Void> resetPassword(HttpServletRequest request,
                                              @RequestBody ResetPasswordDTO resetPasswordDTO) throws ServiceException {
        userService.resetPassword(request, resetPasswordDTO);

        return ResponseEntity.ok().build();
    }


}
