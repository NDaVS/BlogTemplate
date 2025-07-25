package ru.happines.springbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.happines.springbackend.dto.request.PostDTO;
import ru.happines.springbackend.dto.response.PostResponseDTO;
import ru.happines.springbackend.mapper.PostMapper;
import ru.happines.springbackend.model.Post;
import ru.happines.springbackend.model.enums.PostStatus;
import ru.happines.springbackend.service.PostService;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Post> posts = postService.findAll(PageRequest.of(page, size));

        return ResponseEntity.ok(posts.map(postMapper::toDto)); // проверить формат ответа
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postMapper.toDto(postService.findById(id)));
    }

    @GetMapping("/by/user/{userId}")
    public ResponseEntity<Page<PostResponseDTO>> findByUserId(@PathVariable long userId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.findAllByUserId(userId, PageRequest.of(page, size));

        return ResponseEntity.ok(posts.map(postMapper::toDto));// проверить формат ответа
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostDTO postDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postMapper.toDto(postService.create(postDTO)));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable long postId,
                                                      @RequestBody PostDTO postDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postMapper.toDto(postService.update(postId, postDTO)));
    }

    @PatchMapping("/moderate/{postId}")
    public ResponseEntity<PostResponseDTO> modifyPost(@PathVariable long postId,
                                                      @RequestParam("status") PostStatus status) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postMapper.toDto(postService.moderate(postId, status)));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable long postId) {
        postService.delete(postId);

        return ResponseEntity.noContent().build();
    }
}
