package ru.happines.springbackend.mapper;

import org.springframework.stereotype.Component;
import ru.happines.springbackend.dto.response.UserResponseDTO;
import ru.happines.springbackend.model.Post;
import ru.happines.springbackend.model.User;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponseDTO toDto(final User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setIsEmailVerified(user.isEmailVerified());

        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setMiddleName(user.getMiddleName());

        userResponseDTO.setFullName(user.getFullName());
        userResponseDTO.setInitials(user.getInitials());

        userResponseDTO.setRole(user.getRole().getName().name());
        userResponseDTO.setPostIds(user.getPosts().stream().map(Post::getId).collect(Collectors.toList()));

        return  userResponseDTO;
    }
}
