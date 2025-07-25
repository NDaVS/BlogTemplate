package ru.happines.springbackend.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {
    private String username;
    private String email;
    private Boolean isEmailVerified;
    private String firstName;
    private String lastName;
    private String middleName;

    private String fullName;
    private String initials;

    private String role;
    private List<Long> postIds;
}
