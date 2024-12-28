package com.social_media_platform.dto.request;

import lombok.Getter;
import lombok.Setter;


public class SearchRequest {

    private String keyword;

    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
