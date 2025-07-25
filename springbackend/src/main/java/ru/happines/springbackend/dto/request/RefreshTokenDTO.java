package ru.happines.springbackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;


public class RefreshTokenDTO {

    @NotBlank
    @Schema(example = "refreshToken", description = "Refresh token")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }
}