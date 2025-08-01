package ru.happines.springbackend.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User(new CreateUserDTO("Доминик", "deCoke", "555", "admin@email.com", passwordEncoder.encode("passwd"), "admin"));
            admin.setRole(roleRepository.findByName(RoleType.ADMIN).orElseThrow(() -> new Exception("invalid admin role")));
            admin.setEmailVerified(true);
            userRepository.save(admin);
        }
    }
}
