package ru.happines.springbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.happines.springbackend.dto.CreateUserDTO;

import java.util.List;

@Entity
@Data
@Table(name = "scihub_users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonBackReference
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Post> posts;


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
