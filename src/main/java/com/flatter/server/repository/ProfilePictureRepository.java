package com.flatter.server.repository;

import com.flatter.server.domain.ProfilePicture;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProfilePicture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {

}
