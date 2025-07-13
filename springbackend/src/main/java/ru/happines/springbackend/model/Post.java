package ru.happines.springbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.happines.springbackend.dto.PostDTO;
import ru.happines.springbackend.model.enums.PostStatus;

import java.util.List;

@Entity
@Data
@Table(name = "posts")
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    @Column(columnDefinition = "TEXT")
    private List<String> image_paths;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Post(PostDTO postDTO, User author) {
        status = PostStatus.MODERATION;
        image_paths = postDTO.getImage_paths();
        title = postDTO.getTitle();
        content = postDTO.getContent();
        user = author;
    }

    public void Update(PostDTO postDTO) {
        if (postDTO.getImage_paths() != null)
            image_paths = postDTO.getImage_paths();

        if (postDTO.getTitle() != null)
            title = postDTO.getTitle();

        if (postDTO.getContent() != null)
            content = postDTO.getContent();
    }
}
