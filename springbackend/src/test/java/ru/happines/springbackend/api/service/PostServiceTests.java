package ru.happines.springbackend.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.happines.springbackend.dto.request.CreateUserDTO;
import ru.happines.springbackend.dto.request.PostDTO;
import ru.happines.springbackend.model.Post;
import ru.happines.springbackend.model.Role;
import ru.happines.springbackend.model.User;
import ru.happines.springbackend.model.enums.PostStatus;
import ru.happines.springbackend.model.enums.RoleType;
import ru.happines.springbackend.repository.PostRepository;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.service.Implementation.PostServiceImp;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImp postService;

    private PostDTO postDto;
    private PostDTO updatePostDto;

    private User user;

    @BeforeEach
    public void init() {
        CreateUserDTO userDto = CreateUserDTO.builder()
                .email("sample@email.com")
                .hashedPassword("password")
                .username("username")
                .firstName("Peter")
                .middleName("de")
                .lastName("Happiness")
                .build();

        postDto = PostDTO.builder()
                .title("new post")
                .content("Some content")
                .image_paths(List.of("elf", "dwarf"))
                .username(userDto.getUsername())
                .build();
        updatePostDto = PostDTO.builder()
                .title("real new post")
                .content("Some new content")
                .image_paths(List.of("dwarf"))
                .username(userDto.getUsername())
                .build();

        Role role = new Role();
        role.setId(1L);
        role.setName(RoleType.AUTHOR);

        user = new User(userDto);
        user.setRole(role);
        user.setId(2L);
    }

    private void validatePost(Post post, PostDTO postDTO) {
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo(postDTO.getTitle());
        assertThat(post.getContent()).isEqualTo(postDTO.getContent());
        assertThat(post.getImage_paths()).isEqualTo(postDTO.getImage_paths());
        assertThat(post.getUser()).isEqualTo(user);
    }

    @Test
    public void PostService_CreatePost_ReturnNewPost() {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        user = userRepository.save(user);

        Post newPost = postService.create(postDto);

        validatePost(newPost, postDto);
    }

    @Test
    public void PostService_UpdatePost_ReturnUpdatedPost() {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        user = userRepository.save(user);
        Post post = new Post(postDto, user);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        post = postService.create(postDto);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Post updatedPost = postService.update(post.getId(), updatePostDto);

        validatePost(updatedPost, updatePostDto);
    }

    @Test
    public void PostService_DeletePost_CheckIsNotExists() {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        user = userRepository.save(user);
        Post post = new Post(postDto, user);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        post = postService.create(postDto);

        postService.delete(post.getId());

        boolean isExists = postRepository.existsById(post.getId());
        assertThat(isExists).isFalse();
    }

    @Test
    public void PostService_ModeratePost_returnPost() {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        user = userRepository.save(user);
        Post post = new Post(postDto, user);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        post = postService.create(postDto);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Post moderatedPost = postService.moderate(post.getId(), PostStatus.PUBLISHED);

        validatePost(moderatedPost, postDto);
        assertThat(moderatedPost.getStatus()).isEqualTo(PostStatus.PUBLISHED);
    }

    @Test
    public void PostService_FindPostById_returnPost() {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        user = userRepository.save(user);
        Post post = new Post(postDto, user);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        post = postService.create(postDto);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Post savedPost = postService.findById(post.getId());

        validatePost(savedPost, postDto);
    }

    @Test
    public void Post_Service_FindAll_returnAllPosts() {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        user = userRepository.save(user);
        Post post = new Post(postDto, user);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        Post post1 = postService.create(postDto);
        Post post2 = postService.create(updatePostDto);

        Page<Post> mockPage = new PageImpl<>(List.of(post1, post2));
        when(postRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
        Page<Post> posts = postService.findAll(PageRequest.of(0, 10));

        validatePost(posts.getContent().get(0), postDto);
        validatePost(posts.getContent().get(1), updatePostDto);
    }

    @Test
    public void Post_Service_FindAllByUsername_returnAllPosts() {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        user = userRepository.save(user);
        Post post = new Post(postDto, user);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        Post post1 = postService.create(postDto);
        Post post2 = postService.create(updatePostDto);

        Page<Post> mockPage = new PageImpl<>(List.of(post1, post2));
        when(postRepository.findAllByUser_Username(any(String.class), any(Pageable.class))).thenReturn(mockPage);
        Page<Post> posts = postService.findAllByUsername(user.getUsername(), PageRequest.of(0, 10));

        validatePost(posts.getContent().get(0), postDto);
        validatePost(posts.getContent().get(1), updatePostDto);
    }

}
