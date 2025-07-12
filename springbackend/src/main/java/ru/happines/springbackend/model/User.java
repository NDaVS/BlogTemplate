package ru.happines.springbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.happines.springbackend.dto.CreateUserDTO;

@Entity
@Data
@Table(name = "scihub_users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String hashedPassword;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String middleName;
    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonBackReference
    private Role role;

    public User(CreateUserDTO userDTO) {
        username = userDTO.getUsername();
        hashedPassword = userDTO.getHashedPassword();
        email = userDTO.getEmail();
        firstName = userDTO.getFirstName();
        lastName = userDTO.getLastName();
        middleName = userDTO.getMiddleName();

    }

    public String getFullName() {
        return lastName + " " + firstName + " " + middleName;
    }

    public String getInitials() {
        return lastName + " " + firstName.charAt(0) + " " + middleName.charAt(0);
    }


}
