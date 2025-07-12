package ru.happines.springbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    private String firstName;
    private String lastName;
    private String middleName;

    private String email;
    private String hashedPassword;
    private String username;

}
