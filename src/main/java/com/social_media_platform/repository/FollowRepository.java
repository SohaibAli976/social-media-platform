package com.social_media_platform.repository;

import com.social_media_platform.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // Find a specific follow relationship between two users
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // Find followers of a user
    List<Follow> findByFollowingId(Long followingId);

    // Find users followed by a specific user
    List<Follow> findByFollowerId(Long followerId);
}

