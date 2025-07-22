package ru.happines.springbackend.service.Implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.happines.springbackend.dto.CreateUserDTO;
import ru.happines.springbackend.exception.ErrorCode;
import ru.happines.springbackend.exception.ServiceException;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public User findByUsername(String username) throws ServiceException {
        return userRepository.findByUsername(username).orElseThrow(() -> new ServiceException(ErrorCode.BAD_REQUEST_PARAMS, "exception.user.notFound"));
    }

    @Override
    public User findByEmail(String email) throws ServiceException {
        return userRepository.findByEmail(email).orElseThrow(() -> new ServiceException(ErrorCode.BAD_REQUEST_PARAMS, "exception.user.notFound"));
    }


    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
