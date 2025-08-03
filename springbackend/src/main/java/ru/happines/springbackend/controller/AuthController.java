package ru.happines.springbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.dto.request.auth.RecoveryPasswordDTO;
import ru.happines.springbackend.dto.request.auth.ResetPasswordDTO;
import ru.happines.springbackend.dto.response.UserResponseDTO;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.mapper.UserMapper;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.service.AuthService;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("signup")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody CreateUserDTO userDTO) throws ServiceException {
        User user = authService.signup(userDTO);

        return ResponseEntity.ok(userMapper.toDto(user));
    }



    @PostMapping("/password/recovery")
    public ResponseEntity<Void> recoveryPassword(@RequestParam String username) throws ServiceException {

        authService.sendPasswordRecoveryToken(username);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/recovery/validate")
    public ResponseEntity<Void> validatePassword(@RequestParam String token) throws ServiceException {
        authService.validateRecoveryToken(token);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/password/recovery")
    public ResponseEntity<Void> recoveryPassword(@RequestBody RecoveryPasswordDTO recoveryPasswordDTO) throws ServiceException {
        authService.recoveryPassword(recoveryPasswordDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("email/validate")
    public ResponseEntity<Void> validateEmail(@RequestParam String token) throws ServiceException {
        authService.validateEmail(token);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
