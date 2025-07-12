package ru.happines.springbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.happines.springbackend.dto.CreateUserDTO;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
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
            Role adminRole = new Role();
            adminRole.setName(RoleType.ADMIN);

            Role localAdminRole = new Role();
            localAdminRole.setName(RoleType.LOCAL_ADMIN);

            Role moderatorRole = new Role();
            moderatorRole.setName(RoleType.MODERATOR);

            Role authorRole = new Role();
            authorRole.setName(RoleType.AUTHOR);

            roleRepository.saveAll(List.of(authorRole, adminRole, localAdminRole, moderatorRole));

            //Инициализация админа
            User admin = new User(new CreateUserDTO("Доминик", "deCoke", "555", "admin@email.com", "passwd", "admin"));
            admin.setRole(adminRole);
            userRepository.save(admin);
        }
    }
}
