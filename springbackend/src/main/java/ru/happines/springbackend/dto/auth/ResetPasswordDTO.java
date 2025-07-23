package ru.happines.springbackend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
