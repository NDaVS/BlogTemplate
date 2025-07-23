package ru.happines.springbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.happines.springbackend.dto.auth.ResetPasswordDTO;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("password/reset")
    public ResponseEntity<Void> resetPassword(HttpServletRequest request,
                                                @RequestBody ResetPasswordDTO resetPasswordDTO) throws ServiceException {

        authService.resetPassword(request, resetPasswordDTO);
        return ResponseEntity.ok().build();
    }
}
