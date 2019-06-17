package com.flatter.server.repository;

import com.flatter.server.domain.Album;
import com.flatter.server.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Photo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> getAllByAlbum(Album album);
}
