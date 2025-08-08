package ru.happines.springbackend.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.model.EmailVerificationToken;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.EmailVerificationTokenRepository;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmailVerificationTokenRepositoryTests {
    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private CreateUserDTO userDTO;
    private long tokenExpirationTime = 1;
    private String rawToken;

    private User createUserWithRole(CreateUserDTO dto, Role role) {
        User user = new User(dto);
        user.setRole(role);
        return userRepository.save(user);
    }

    private void validateEmailToken(EmailVerificationToken token, User user) {
        assertThat(token).isNotNull();
        assertThat(token.getToken()).isEqualTo(rawToken);
        assertThat(token.getId()).isGreaterThan(0);
        assertThat(token.getUser()).isEqualTo(user);
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
        rawToken = UUID.randomUUID().toString();
    }

    @Test
    public void EmailVerificationRepository_SaveToken_ReturnNotNull() throws InterruptedException {
        Role role = roleRepository.findByName(RoleType.AUTHOR).orElse(null);
        User user = createUserWithRole(userDTO, role);

        EmailVerificationToken token = new EmailVerificationToken(rawToken, user, tokenExpirationTime);
        EmailVerificationToken savedToken = emailVerificationTokenRepository.save(token);

        validateEmailToken(token, user);

        Thread.sleep(1001);
        assertThat(savedToken.getExpiryDate()).isBefore(Calendar.getInstance().getTime());
    }

    @Test
    public void EmailVerificationRepository_FindByToken_ReturnToken() {
        Role role = roleRepository.findByName(RoleType.AUTHOR).orElse(null);
        User user = createUserWithRole(userDTO, role);

        EmailVerificationToken token = new EmailVerificationToken(rawToken, user, tokenExpirationTime);
        emailVerificationTokenRepository.save(token);
        EmailVerificationToken savedToken = emailVerificationTokenRepository.findByToken(token.getToken()).orElse(null);

        validateEmailToken(savedToken, user);
    }

    @Test
    public void EmailVerificationRepository_DeleteToken_ReturnExistsByIdIsFalse() {
        Role role = roleRepository.findByName(RoleType.AUTHOR).orElse(null);
        User user = createUserWithRole(userDTO, role);
        EmailVerificationToken token = new EmailVerificationToken(rawToken, user, tokenExpirationTime);

        EmailVerificationToken savedToken = emailVerificationTokenRepository.save(token);
        emailVerificationTokenRepository.delete(savedToken);

        assertThat(emailVerificationTokenRepository.existsById(savedToken.getId())).isFalse();
    }
}
