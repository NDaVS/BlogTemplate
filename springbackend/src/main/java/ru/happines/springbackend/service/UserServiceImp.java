package ru.happines.springbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.happines.springbackend.dto.CreateUserDTO;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<String> findAllNames() {
        return userRepository.findAll().stream().map(User::getFullName).collect(Collectors.toList());
    }

    @Override
    public User create(CreateUserDTO userDTO) {
        User user = new User(userDTO);
        user.setRole(roleRepository.findByName(RoleType.AUTHOR).orElseThrow(() -> new IllegalStateException("Role AUTHOR is not found")));
        userRepository.save(user);
        return user;
    }
}
