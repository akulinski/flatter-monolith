package com.flatter.server.repository;

import com.flatter.server.domain.ProfilePicture;
import com.flatter.server.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the ProfilePicture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    Optional<ProfilePicture> findAllByUser(User user);
}
