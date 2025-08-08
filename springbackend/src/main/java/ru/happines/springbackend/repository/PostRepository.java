package ru.happines.springbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.happines.springbackend.model.Post;
import ru.happines.springbackend.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser_Username(String userUsername, Pageable pageable);
}
