package ru.happines.springbackend.service.Implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.happines.springbackend.dto.request.PostDTO;
import ru.happines.springbackend.model.Post;
import ru.happines.springbackend.model.enums.PostStatus;
import ru.happines.springbackend.repository.PostRepository;
import ru.happines.springbackend.repository.UserRepository;
import ru.happines.springbackend.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public Post create(PostDTO postDTO) {
        Post post = new Post(
                postDTO,
                userRepository.findByUsername(postDTO.getUsername())
                        .orElseThrow(
                                () -> new IllegalArgumentException("There is no user with username " + postDTO.getUsername())
                        )
        );
        postRepository.save(post);

        return post;
    }

    @Override
    public Post update(Long postId, PostDTO postDTO) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("\"There is no post with id" + postId)
        );
        post.update(postDTO);
        postRepository.save(post);

        return post;
    }

    @Override
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Post moderate(Long postId, PostStatus postStatus) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("\"There is no post with id" + postId)
        );
        post.setStatus(postStatus);
        postRepository.save(post);

        return post;
    }

    @Override
    public Post findById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("\"There is no post with id" + postId)
        );
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> findAllByUsername(String username, Pageable pageable) {
        return postRepository.findAllByUser_Username(username, pageable);
    }
}
