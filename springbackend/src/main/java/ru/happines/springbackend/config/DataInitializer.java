package ru.happines.springbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0 && roleRepository.count() == 0) {
            // Инициализация ролей
            Role authorRole = new Role();
            authorRole.setName("AUTHOR");

            Role adminRole = new Role();
            adminRole.setName("ADMIN");

            Role moderatorRole = new Role();
            moderatorRole.setName("MODERATOR");

            roleRepository.saveAll(List.of(authorRole, adminRole, moderatorRole));

            //Инициализация админа
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@email.com");
            admin.setFirstName("Доминик");
            admin.setLastName("deCoke");
            admin.setMiddleName("555");
            admin.setHashed_password("password");
            admin.setRole(authorRole);
            userRepository.save(admin);
        }
    }
}
