package ru.happines.springbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<String> findAllNames() {
        return roleRepository.findAll().stream().map(Role::getName).toList();
    }
}
