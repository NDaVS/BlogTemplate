package ru.happines.springbackend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecoveryPasswordDTO {
    private String recoveryToken;
    private String newPassword;
    private String confirmNewPassword;
}
