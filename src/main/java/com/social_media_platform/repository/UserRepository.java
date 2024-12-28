package com.social_media_platform.repository;

import com.social_media_platform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    // Custom search functionality
    Page<User> findByUsernameContainingOrEmailContainingOrBioContaining(String username,
                                                                        String email,
                                                                        String bio,
                                                                        Pageable pageable);
}

