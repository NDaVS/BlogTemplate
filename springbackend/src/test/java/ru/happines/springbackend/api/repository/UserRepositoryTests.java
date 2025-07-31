package ru.happines.springbackend.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private CreateUserDTO userDTO;

    private Role createRole() {
        Role role = new Role();
        role.setName(RoleType.AUTHOR);
        return roleRepository.save(role);
    }

    private User createUserWithRole(CreateUserDTO dto, Role role) {
        User user = new User(dto);
        user.setRole(role);
        return userRepository.save(user);
    }

    private void validateUser(Role role, User savedUser) {
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(role);
        assertThat(savedUser.getUsername()).isEqualTo(userDTO.getUsername());
        assertThat(savedUser.getFirstName()).isEqualTo(userDTO.getFirstName());
        assertThat(savedUser.getMiddleName()).isEqualTo(userDTO.getMiddleName());
        assertThat(savedUser.getLastName()).isEqualTo(userDTO.getLastName());
        assertThat(savedUser.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository,
                                  RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @BeforeEach
    public void init() {
        userDTO = CreateUserDTO.builder()
                .email("sample@email.com")
                .hashedPassword("password")
                .username("username")
                .firstName("Peter")
                .middleName("de")
                .lastName("Happiness")
                .build();

    }

    @Test
    public void UserRepository_SaveAll_ReturnSavedUser() {
        Role role = createRole();
        User savedUser = createUserWithRole(userDTO, role);

        validateUser(role, savedUser);
    }

    @Test
    public void UserRepository_FindByUsername_ReturnUserNotNull() {
        Role role = createRole();
        createUserWithRole(userDTO, role);
        User savedUser = userRepository.findByUsername(userDTO.getUsername()).orElse(null);

        validateUser(role, savedUser);
    }

    @Test
    public void UserRepository_FindByEmail_ReturnUserNotNull() {
        Role role = createRole();
        createUserWithRole(userDTO, role);
        User savedUser = userRepository.findByEmail(userDTO.getEmail()).orElse(null);

        validateUser(role, savedUser);
    }

    @Test
    public void UserRepository_IsExistsByEmail_ReturnTrue() {
        Role role = createRole();
        createUserWithRole(userDTO, role);
        boolean isUserExists = userRepository.existsByEmail(userDTO.getEmail());

        assertThat(isUserExists).isEqualTo(true);
    }
}
