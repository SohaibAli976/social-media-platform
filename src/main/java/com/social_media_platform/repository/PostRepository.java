package com.social_media_platform.repository;

import com.social_media_platform.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId);

    // Custom search functionality
    Page<Post> findByContentContainingOrTitleContaining(String content,
                                                        String title,
                                                        Pageable pageable);
}

