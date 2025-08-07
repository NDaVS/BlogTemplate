package ru.happines.springbackend.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.service.Implementation.RoleServiceImp;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleServiceImp roleService;

    private CreateUserDTO userDTO;
    private User user;
    private Role role;
    private Role new_role;

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

        new_role = new Role();
        new_role.setId(4L);
        new_role.setName(RoleType.MODERATOR);

        user = new User(userDTO);
        user.setRole(role);
        user.setId(2L);
    }

    @Test
    public void RoleService_SetRoleForUser_ReturnVoid(){
        User new_user = new User(userDTO);
        new_user.setId(3L);
        new_user.setRole(role);
        when(userRepository.save(new_user)).thenReturn(new_user);
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(new_user));
        when(roleRepository.findByName(RoleType.MODERATOR)).thenReturn(Optional.of(new_role));

        userRepository.save(new_user);
        roleService.setRole(userDTO.getUsername(), RoleType.MODERATOR);
        User savedUser = userRepository.findByUsername(userDTO.getUsername()).orElse(null);

        assertThat(savedUser).isNotNull();
        assertThat(new_user.getRole().getName()).isEqualTo(RoleType.MODERATOR);
    }
}
