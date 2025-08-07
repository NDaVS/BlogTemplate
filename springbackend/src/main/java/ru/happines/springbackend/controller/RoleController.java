package ru.happines.springbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;


    @GetMapping
    public List<RoleType> listRoles() {
        return roleService.findAllNames();
    }

    @GetMapping("/{id}")
    public Role getRole(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @PatchMapping
    public ResponseEntity<Role> updateRole(
            @RequestParam("username") String username,
            @RequestParam("role_type") RoleType roleType
    ) {
        roleService.setRole(username, roleType);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

