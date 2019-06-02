package com.flatter.server.web.rest;

import com.flatter.server.domain.Album;
import com.flatter.server.domain.Photo;
import com.flatter.server.repository.AlbumRepository;
import com.flatter.server.repository.PhotoRepository;
import com.flatter.server.service.PictureService;
import com.flatter.server.web.rest.errors.BadRequestAlertException;
import domain.PhotoDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.flatter.server.domain.Photo}.
 */
@RestController
@RequestMapping("/api")
public class PhotoResource {

    private final Logger log = LoggerFactory.getLogger(PhotoResource.class);

    private static final String ENTITY_NAME = "photo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhotoRepository photoRepository;

    private final AlbumRepository albumRepository;

    private final PictureService pictureService;

    public PhotoResource(PhotoRepository photoRepository, AlbumRepository albumRepository, PictureService pictureService) {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
        this.pictureService = pictureService;
    }

    /**
     * {@code POST  /photos} : Create a new photo.
     *
     * @param photo the photo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new photo, or with status {@code 400 (Bad Request)} if the photo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/photos")
    public ResponseEntity<Photo> createPhoto(@Valid @RequestBody Photo photo) throws URISyntaxException {
        log.debug("REST request to save Photo : {}", photo);
        if (photo.getId() != null) {
            throw new BadRequestAlertException("A new photo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Photo result = photoRepository.save(photo);
        return ResponseEntity.created(new URI("/api/photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /photos} : Updates an existing photo.
     *
     * @param photo the photo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photo,
     * or with status {@code 400 (Bad Request)} if the photo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the photo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/photos")
    public ResponseEntity<Photo> updatePhoto(@Valid @RequestBody Photo photo) throws URISyntaxException {

        log.debug("REST request to update Photo : {}", photo);

        if (photo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Photo result = photoRepository.save(photo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, photo.getId().toString()))
            .body(result);
    }


    @PostMapping("/add-photo-by-album-title")
    public ResponseEntity<Photo> addPhotoByAlbumTitle(PhotoDTO photoDTO) throws IOException {
        log.debug("REST request to upload photo: {}", photoDTO);

        Album album = albumRepository.findByTitle(photoDTO.getAlbumTitle()).orElseThrow(() -> new IllegalStateException("Album not found"));

        Photo photo = new Photo();
        photo.setAlbum(album);
        photo.setDescription(photoDTO.getDescription());
        photo.setHeight(photoDTO.getHeight());
        photo.setWidth(photoDTO.getWidth());
        photo.setImageContentType(photoDTO.getImageContentType());
        photo.setTaken(photoDTO.getTaken());
        photo.setUploaded(new Date().toInstant());
        photo.setTitle(photoDTO.getPhotoTitle());
        photo.setImage(photoDTO.getBytes());
        pictureService.scaleDownPicture(photo);

        return ResponseEntity.ok(photo);
    }

    /**
     * {@code GET  /photos} : get all the photos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of photos in body.
     */
    @GetMapping("/photos")
    public ResponseEntity<List<Photo>> getAllPhotos(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Photos");
        Page<Photo> page = photoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /photos/:id} : get the "id" photo.
     *
     * @param id the id of the photo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the photo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/photos/{id}")
    public ResponseEntity<Photo> getPhoto(@PathVariable Long id) {
        log.debug("REST request to get Photo : {}", id);
        Optional<Photo> photo = photoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(photo);
    }

    /**
     * {@code DELETE  /photos/:id} : delete the "id" photo.
     *
     * @param id the id of the photo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/photos/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        log.debug("REST request to delete Photo : {}", id);
        photoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
