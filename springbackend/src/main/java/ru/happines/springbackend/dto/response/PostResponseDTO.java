package ru.happines.springbackend.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PostResponseDTO {
    private long id;
    private String status;
    private List<String> images_paths;
    private String title;
    private String content;
    private String owner_username;
    private String owner_initials;
}
