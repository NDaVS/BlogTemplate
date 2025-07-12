package ru.happines.springbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "scihub_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String hashed_password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String middleName;
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    public String getFullName() {
        return lastName + " " + firstName + " " + middleName;
    }

    public String getInitials() {
        return lastName + " " + firstName.charAt(0) + " " + middleName.charAt(0);
    }


}
