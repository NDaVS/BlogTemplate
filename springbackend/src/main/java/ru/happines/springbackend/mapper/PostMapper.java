package ru.happines.springbackend.mapper;

import org.springframework.stereotype.Component;
import ru.happines.springbackend.dto.response.PostResponseDTO;
import ru.happines.springbackend.model.Post;

@Component
public class PostMapper {
    public PostResponseDTO toDto(Post post){
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(post.getId());
        postResponseDTO.setStatus(post.getStatus().toString());
        post.setImage_paths(post.getImage_paths());
        postResponseDTO.setTitle(post.getTitle());
        postResponseDTO.setContent(post.getContent());

        postResponseDTO.setOwner_username(post.getUser().getUsername());
        postResponseDTO.setOwner_initials(post.getUser().getInitials());

        return postResponseDTO;
    }
}
