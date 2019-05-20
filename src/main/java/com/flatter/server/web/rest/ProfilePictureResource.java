package com.flatter.server.web.rest;

import com.flatter.server.domain.ProfilePicture;
import com.flatter.server.repository.ProfilePictureRepository;
import com.flatter.server.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.flatter.server.domain.ProfilePicture}.
 */
@RestController
@RequestMapping("/api")
public class ProfilePictureResource {

    private final Logger log = LoggerFactory.getLogger(ProfilePictureResource.class);

    private static final String ENTITY_NAME = "profilePicture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfilePictureRepository profilePictureRepository;

    public ProfilePictureResource(ProfilePictureRepository profilePictureRepository) {
        this.profilePictureRepository = profilePictureRepository;
    }

    /**
     * {@code POST  /profile-pictures} : Create a new profilePicture.
     *
     * @param profilePicture the profilePicture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profilePicture, or with status {@code 400 (Bad Request)} if the profilePicture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profile-pictures")
    public ResponseEntity<ProfilePicture> createProfilePicture(@Valid @RequestBody ProfilePicture profilePicture) throws URISyntaxException {
        log.debug("REST request to save ProfilePicture : {}", profilePicture);
        if (profilePicture.getId() != null) {
            throw new BadRequestAlertException("A new profilePicture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfilePicture result = profilePictureRepository.save(profilePicture);
        return ResponseEntity.created(new URI("/api/profile-pictures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /profile-pictures} : Updates an existing profilePicture.
     *
     * @param profilePicture the profilePicture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profilePicture,
     * or with status {@code 400 (Bad Request)} if the profilePicture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profilePicture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profile-pictures")
    public ResponseEntity<ProfilePicture> updateProfilePicture(@Valid @RequestBody ProfilePicture profilePicture) throws URISyntaxException {
        log.debug("REST request to update ProfilePicture : {}", profilePicture);
        if (profilePicture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfilePicture result = profilePictureRepository.save(profilePicture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, profilePicture.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /profile-pictures} : get all the profilePictures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profilePictures in body.
     */
    @GetMapping("/profile-pictures")
    public List<ProfilePicture> getAllProfilePictures() {
        log.debug("REST request to get all ProfilePictures");
        return profilePictureRepository.findAll();
    }

    /**
     * {@code GET  /profile-pictures/:id} : get the "id" profilePicture.
     *
     * @param id the id of the profilePicture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profilePicture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profile-pictures/{id}")
    public ResponseEntity<ProfilePicture> getProfilePicture(@PathVariable Long id) {
        log.debug("REST request to get ProfilePicture : {}", id);
        Optional<ProfilePicture> profilePicture = profilePictureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(profilePicture);
    }

    /**
     * {@code DELETE  /profile-pictures/:id} : delete the "id" profilePicture.
     *
     * @param id the id of the profilePicture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profile-pictures/{id}")
    public ResponseEntity<Void> deleteProfilePicture(@PathVariable Long id) {
        log.debug("REST request to delete ProfilePicture : {}", id);
        profilePictureRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
