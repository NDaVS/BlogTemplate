package ru.happines.springbackend.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.RoleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void RoleRepository_SaveAll_ReturnSavedRole() {
        Role role = Role.builder().name(RoleType.AUTHOR).build();

        Role savedRole = roleRepository.save(role);

        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void RoleRepository_FindByName_ReturnRoleNotNull() {
        Role role = Role.builder().name(RoleType.AUTHOR).build();
        roleRepository.save(role);

        Role roleByName = roleRepository.findByName(RoleType.AUTHOR).get();
        assertThat(roleByName).isNotNull();
    }
}
