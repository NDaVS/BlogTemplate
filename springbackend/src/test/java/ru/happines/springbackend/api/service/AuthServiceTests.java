package ru.happines.springbackend.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.EmailVerificationToken;
import ru.happines.springbackend.model.PasswordRecoveryToken;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.EmailVerificationTokenRepository;
import ru.happines.springbackend.repository.PasswordRecoveryTokenRepository;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.service.Implementation.AuthServiceImpl;
import ru.happines.springbackend.service.Implementation.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;
    @Mock
    private PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private UserServiceImpl userService;
    private User user;
    private Role role;
    CreateUserDTO userDto;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(authService, "tokenExpirationTime", 1800L);
        userDto = CreateUserDTO.builder()
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

        user = new User(userDto);
        user.setRole(role);
        user.setId(1L);
    }

    @Test
    public void AuthService_Signup_ReturnUser() {

        when(emailVerificationTokenRepository.save(any(EmailVerificationToken.class))).thenReturn(null);
        when(userService.create(userDto)).thenReturn(user);
        User newUser = authService.signup(userDto);

        assertThat(newUser).isNotNull();
        assertThat(newUser.getRole().getName()).isEqualTo(RoleType.AUTHOR);
        assertThat(newUser.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(newUser.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(newUser.getMiddleName()).isEqualTo(userDto.getMiddleName());
        assertThat(newUser.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(newUser.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(newUser.getId()).isGreaterThan(0);
    }

    @Test
    public void AuthService_SendPasswordRecoveryToken_IsInDB() throws ServiceException {
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(user));
        when(passwordRecoveryTokenRepository.save(any(PasswordRecoveryToken.class))).thenReturn(null);
        String str =  authService.sendPasswordRecoveryToken(userDto.getUsername());
        assertThat(str).isNotNull();
    }

    @Test
    public void AuthService_ValidatePasswordRecoveryToken_() {
    }

    @Test
    public void AuthService_validateEmail_IsEmailValidated() {
    }

    @Test
    public void AuthService_RecoveryPassword_isPasswordChanged() {
    }

}
