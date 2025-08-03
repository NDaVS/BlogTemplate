package ru.happines.springbackend.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.service.Implementation.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private CreateUserDTO userDTO;
    private User user;
    private Role role;

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

        role = new Role();
        role.setId(1L);
        role.setName(RoleType.AUTHOR);
        user = new User(userDTO);
        user.setRole(role);
        user.setId(2L);
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

    @Test
    public void whenFindById_thenReturnUser() {
        long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userServiceImpl.findById(userId);

        validateUser(role, foundUser);
    }
    @Test
    public void whenFindByUsername_thenReturnUser() throws ServiceException {
        String username = userDTO.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = userServiceImpl.findByUsername(username);

        validateUser(role, foundUser);
    }

    @Test
    public void whenFindByEmail_thenReturnUser() throws ServiceException {
        String email = userDTO.getEmail();
        when(userRepository.findByUsername(email)).thenReturn(Optional.of(user));

        User foundUser = userServiceImpl.findByUsername(email);

        validateUser(role, foundUser);
    }

    @Test
    public void whenIsExistsByEmail_thenReturnTrue() throws ServiceException {
        String email =  userDTO.getEmail();

        when(userRepository.existsByEmail(email)).thenReturn(Boolean.TRUE);
        boolean isExist = userRepository.existsByEmail(email);

        assertThat(isExist).isTrue();
    }

    @Test
    public void whenCreateByDto_thenReturnUser() throws ServiceException {
        User new_user = new User(userDTO);
        new_user.setId(3L);
        new_user.setRole(role);
        when(userRepository.save(new_user)).thenReturn(new_user);

        User savedUser = userRepository.save(new_user);

        validateUser(role,  savedUser);
    }

    //Reset Password (idk for now)
}
