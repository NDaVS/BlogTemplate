package ru.happines.springbackend.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.dto.request.PostDTO;
import ru.happines.springbackend.model.Post;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.PostRepository;
import ru.happines.springbackend.repository.RoleRepository;
import ru.happines.springbackend.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PostRepositoryTests {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private PostDTO postDTO_1;
    private PostDTO postDTO_2;
    private CreateUserDTO userDTO;

    private Role createRole() {
        Role role = new Role();
        role.setName(RoleType.AUTHOR);
        return roleRepository.save(role);
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

    @BeforeEach
    public void init() {
        postDTO_1 = PostDTO.builder()
                .title("title")
                .content("content")
                .user_id(1)
                .image_paths(List.of("A", "B"))
                .build();

        postDTO_2 = PostDTO.builder()
                .title("title2")
                .content("content2")
                .user_id(1)
                .image_paths(List.of("AA", "BB"))
                .build();

        userDTO = CreateUserDTO.builder()
                .email("sample@email.com")
                .hashedPassword("password")
                .username("username")
                .firstName("Peter")
                .middleName("de")
                .lastName("Happiness")
                .build();
    }

    @Test
    public void PostRepository_SaveAll_ReturnPostNotNull() {
        Role role = createRole();
        User user = createUserWithRole(userDTO, role);

        Post post = new Post(postDTO_1, user);
        postRepository.save(post);

        validatePost(post, postDTO_1);
    }

    @Test
    public void PostRepository_FindAllByUserId_ReturnPostsNotNull() {
        Role role = createRole();
        User user = createUserWithRole(userDTO, role);

        Post post_1 = new Post(postDTO_1, user);
        Post post_2 = new Post(postDTO_2, user);

        postRepository.saveAll(List.of(post_1, post_2));
        Page<Post> posts = postRepository.findAllByUser_Id(user.getId(), PageRequest.of(0, 10));
        assertThat(posts.getContent().size()).isEqualTo(2);
        validatePost(posts.getContent().getFirst(), postDTO_1);
        validatePost(posts.getContent().get(1), postDTO_2);
    }
}
