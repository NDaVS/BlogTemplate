package ru.happines.springbackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SigninDTO {
    @NotBlank
    @Schema(example = "username", description = "User name")
    private String username;

    @NotBlank
    @Schema(example = "password", description = "User password")
    private String password;
}
