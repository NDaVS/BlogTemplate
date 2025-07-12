package ru.happines.springbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<String> findAllNames() {
        return userRepository.findAll().stream().map(User::getFullName).collect(Collectors.toList());
    }
}
