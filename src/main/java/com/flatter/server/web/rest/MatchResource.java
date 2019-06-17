package com.flatter.server.web.rest;

import com.flatter.server.domain.Match;
import com.flatter.server.repository.MatchRepository;
import com.flatter.server.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/** REST controller for managing {@link com.flatter.server.domain.Match}. */
@RestController
@RequestMapping("/api")
public class MatchResource {

  private final Logger log = LoggerFactory.getLogger(MatchResource.class);

  private static final String ENTITY_NAME = "match";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final MatchRepository matchRepository;

  public MatchResource(MatchRepository matchRepository) {
    this.matchRepository = matchRepository;
  }

  /**
   * {@code POST /matches} : Create a new match.
   *
   * @param match the match to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     match, or with status {@code 400 (Bad Request)} if the match has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/matches")
  public ResponseEntity<Match> createMatch(@RequestBody Match match) throws URISyntaxException {
    log.debug("REST request to save Match : {}", match);
    if (match.getId() != null) {
      throw new BadRequestAlertException(
          "A new match cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Match result = matchRepository.save(match);
    return ResponseEntity.created(new URI("/api/matches/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, false, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /matches} : Updates an existing match.
   *
   * @param match the match to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     match, or with status {@code 400 (Bad Request)} if the match is not valid, or with status
   *     {@code 500 (Internal Server Error)} if the match couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/matches")
  public ResponseEntity<Match> updateMatch(@RequestBody Match match) throws URISyntaxException {
    log.debug("REST request to update Match : {}", match);
    if (match.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Match result = matchRepository.save(match);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, false, ENTITY_NAME, match.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /matches} : get all the matches.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matches in
   *     body.
   */
  @GetMapping("/matches")
  public List<Match> getAllMatches() {
    log.debug("REST request to get all Matches");
    return matchRepository.findAll();
  }

  /**
   * {@code GET /matches/:id} : get the "id" match.
   *
   * @param id the id of the match to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the match, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/matches/{id}")
  public ResponseEntity<Match> getMatch(@PathVariable Long id) {
    log.debug("REST request to get Match : {}", id);
    Optional<Match> match = matchRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(match);
  }

  /**
   * {@code DELETE /matches/:id} : delete the "id" match.
   *
   * @param id the id of the match to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/matches/{id}")
  public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
    log.debug("REST request to delete Match : {}", id);
    matchRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(
                applicationName, false, ENTITY_NAME, id.toString()))
        .build();
  }
}
