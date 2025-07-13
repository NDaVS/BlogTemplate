package ru.happines.springbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.happines.springbackend.dto.PostDTO;
import ru.happines.springbackend.model.Post;
import ru.happines.springbackend.model.enums.PostStatus;

public interface PostService {
    Post create(PostDTO postDTO);

    Post update(Long postId, PostDTO postDTO);

    void delete(Long postId);

    Post moderate(Long postId, PostStatus postStatus);

    Post findById(Long postId);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByUserId(long userId, Pageable pageable);

}
