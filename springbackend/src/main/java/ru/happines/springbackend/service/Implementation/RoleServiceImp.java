package ru.happines.springbackend.service.Implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.service.RoleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Role findById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<RoleType> findAllNames() {
        return roleRepository.findAll().stream().map(Role::getName).toList();
    }

    @Override
    public Role setRole(long userId, RoleType roleType) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with ID: " + userId + " - is not found"));
        Role role = roleRepository
                .findByName(roleType)
                .orElseThrow(() -> new IllegalStateException("Role" + roleType + " is not found"));
        user.setRole(role);

        userRepository.save(user);

        return role;
    }
}
