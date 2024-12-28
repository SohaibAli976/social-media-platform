package com.social_media_platform.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequest {

    private Long userId; // Optional, if you want to allow specifying a user to like the post

    // Getters and Setters
}

