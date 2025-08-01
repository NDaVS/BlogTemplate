package ru.happines.springbackend.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.dto.request.PostDTO;
import ru.happines.springbackend.model.Post;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.service.Implementation.UserServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void init() {
        userDTO = CreateUserDTO.builder()
                .email("sample@email.com")
                .hashedPassword("password")
                .username("username")
                .firstName("Peter")
                .middleName("de")
                .lastName("Happiness")
                .build();
        createRole();
    }

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private CreateUserDTO userDTO;

    private void createRole() {
        Role role = new Role();
        role.setName(RoleType.AUTHOR);
        roleRepository.save(role);
    }

    private User createUserWithRole(CreateUserDTO dto, Role role) {
        User user = new User(dto);
        user.setRole(role);
        return userRepository.save(user);
    }

    private void validatePost(Post post, PostDTO postDTO){
        assertThat(post).isNotNull();
        assertThat(post.getId()).isGreaterThan(0);
        assertThat(post.getTitle()).isEqualTo(postDTO.getTitle());
        assertThat(post.getContent()).isEqualTo(postDTO.getContent());
        assertThat(post.getImage_paths()).isEqualTo(postDTO.getImage_paths());
    }





}
