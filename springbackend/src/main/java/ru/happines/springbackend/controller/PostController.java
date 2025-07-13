package ru.happines.springbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.happines.springbackend.dto.PostDTO;
import ru.happines.springbackend.model.Post;
import ru.happines.springbackend.model.enums.PostStatus;
import ru.happines.springbackend.service.PostService;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<Post>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postService.findAll(pageable);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @GetMapping("/by/user/{userId}")
    public ResponseEntity<Page<Post>> findByUserId(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postService.findAllByUserId(userId, pageable);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO) {
        Post post = postService.create(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable long postId, @RequestBody PostDTO postDTO) {
        Post post = postService.update(postId, postDTO);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @PatchMapping("/moderate/{postId}")
    public ResponseEntity<Post> modifyPost(@PathVariable long postId, @RequestParam("status") PostStatus status) {
        Post post = postService.moderate(postId, status);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}
