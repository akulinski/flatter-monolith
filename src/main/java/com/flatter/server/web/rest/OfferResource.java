package com.flatter.server.web.rest;

import com.flatter.server.domain.ClusteringDocument;
import com.flatter.server.domain.Offer;
import com.flatter.server.domain.User;
import com.flatter.server.domain.dto.FullOfferDTO;
import com.flatter.server.domain.elastic.QuestionnaireableOffer;
import com.flatter.server.repository.OfferRepository;
import com.flatter.server.repository.PhotoRepository;
import com.flatter.server.repository.UserRepository;
import com.flatter.server.repository.elastic.ClusteringDocumentRepository;
import com.flatter.server.service.MatchingService;
import com.flatter.server.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.flatter.server.domain.Offer}.
 */
@RestController
@RequestMapping("/api")
public class OfferResource {

    private final Logger log = LoggerFactory.getLogger(OfferResource.class);

    private static final String ENTITY_NAME = "offer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfferRepository offerRepository;

    private final PhotoRepository photoRepository;

    private final MatchingService matchingService;

    private final UserRepository userRepository;

    private final ClusteringDocumentRepository clusteringDocumentRepository;


    public OfferResource(
        OfferRepository offerRepository,
        PhotoRepository photoRepository,
        MatchingService matchingService, UserRepository userRepository, ClusteringDocumentRepository clusteringDocumentRepository) {
        this.offerRepository = offerRepository;
        this.photoRepository = photoRepository;
        this.matchingService = matchingService;
        this.userRepository = userRepository;
        this.clusteringDocumentRepository = clusteringDocumentRepository;
    }

    /**
     * {@code POST /offers} : Create a new offer.
     *
     * @param offer the offer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
     * offer, or with status {@code 400 (Bad Request)} if the offer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/offers")
    public ResponseEntity<Offer> createOffer(@Valid @RequestBody Offer offer, Principal principal)
        throws URISyntaxException {
        log.debug("REST request to save Offer : {}", offer);
        if (offer.getId() != null) {
            throw new BadRequestAlertException(
                "A new offer cannot already have an ID", ENTITY_NAME, "idexists");
        }

        User user = userRepository.findOneByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("Principal name not found in DB"));


        offer.getAlbum().getPhotos().forEach(photo -> {

            photo.setTitle(offer.getAlbum().getTitle());
            photo.setAlbum(offer.getAlbum());

        });

        offer.getAlbum().setOffer(offer);
        offer.getAddress().setOffer(offer);

        offer.setUser(user);


        ClusteringDocument clusteringDocument = new ClusteringDocument();
        QuestionnaireableOffer questionnaireableOffer = setUpOfferFromDTO(offer);
        clusteringDocument.setQuestionnaireable(questionnaireableOffer);
        clusteringDocumentRepository.save(clusteringDocument);

        Offer result = offerRepository.save(offer);
        return ResponseEntity.created(new URI("/api/offers/" + result.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private QuestionnaireableOffer setUpOfferFromDTO(@RequestBody @Valid Offer offer) {
        QuestionnaireableOffer questionnaireableOffer = new QuestionnaireableOffer();
        questionnaireableOffer.setDescription(offer.getDescription());
        questionnaireableOffer.setConstructionYearMax(offer.getConstructionYear());
        questionnaireableOffer.setConstructionYearMin(offer.getConstructionYear());
        questionnaireableOffer.setFurnished(offer.isIsFurnished());
        questionnaireableOffer.setName("offer");
        questionnaireableOffer.setPets(offer.isPets());
        questionnaireableOffer.setRoomAmountMax(offer.getRoomAmount());
        questionnaireableOffer.setRoomAmountMin(offer.getRoomAmount());
        questionnaireableOffer.setSmokingInside(offer.isSmokingInside());
        questionnaireableOffer.setSizeMax(offer.getSize());
        questionnaireableOffer.setSizeMin(offer.getSize());
        questionnaireableOffer.setTotalCostMax(offer.getTotalCost());
        questionnaireableOffer.setTotalCostMin(offer.getTotalCost());
        questionnaireableOffer.setType(offer.getType());
        return questionnaireableOffer;
    }

    /**
     * {@code PUT /offers} : Updates an existing offer.
     *
     * @param offer the offer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
     * offer, or with status {@code 400 (Bad Request)} if the offer is not valid, or with status
     * {@code 500 (Internal Server Error)} if the offer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/offers")
    public ResponseEntity<Offer> updateOffer(@Valid @RequestBody Offer offer)
        throws URISyntaxException {
        log.debug("REST request to update Offer : {}", offer);
        if (offer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Offer result = offerRepository.save(offer);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, false, ENTITY_NAME, offer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET /offers} : get all the offers.
     *
     * @param pageable the pagination information.
     * @param filter   the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offers in body.
     */
    @GetMapping("/offers")
    public ResponseEntity<List<Offer>> getAllOffers(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams
        , UriComponentsBuilder uriBuilder, @RequestParam(required = false) String filter) {

        if ("address-is-null".equals(filter)) {
            log.debug("REST request to get all Offers where address is null");
            return new ResponseEntity<>(
                StreamSupport.stream(offerRepository.findAll().spliterator(), false)
                    .filter(offer -> offer.getAddress() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK);
        }
        if ("album-is-null".equals(filter)) {
            log.debug("REST request to get all Offers where album is null");
            return new ResponseEntity<>(
                StreamSupport.stream(offerRepository.findAll().spliterator(), false)
                    .filter(offer -> offer.getAlbum() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK);
        }
        if ("match-is-null".equals(filter)) {
            log.debug("REST request to get all Offers where match is null");
            return new ResponseEntity<>(
                StreamSupport.stream(offerRepository.findAll().spliterator(), false)
                    .filter(offer -> offer.getMatch() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK);
        }
        log.debug("REST request to get a page of Offers");

        Page<Offer> page = offerRepository.findAll(pageable);

        HttpHeaders headers =
            PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /offers/:id} : get the "id" offer.
     *
     * @param id the id of the offer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offer, or
     * with status {@code 404 (Not Found)}.
     */
    @GetMapping("/offers/{id}")
    public ResponseEntity<Offer> getOffer(@PathVariable Long id) {
        log.debug("REST request to get Offer : {}", id);
        Optional<Offer> offer = offerRepository.findById(id);
        if (offer.isPresent()) {
            final Offer tmp = offer.get();
            tmp.addView();
            offerRepository.save(tmp);
        }
        return ResponseUtil.wrapOrNotFound(offer);
    }

    /**
     * {@code DELETE /offers/:id} : delete the "id" offer.
     *
     * @param id the id of the offer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/offers/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        log.debug("REST request to delete Offer : {}", id);
        offerRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(
                HeaderUtil.createEntityDeletionAlert(
                    applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }


    /**
     * {@code GET /my-offers} : get offers targeted at user
     *
     * @return the {@link ResponseEntity} with List of Offers {@code 200}
     */
    @GetMapping("/mock-my-offers")
    public ResponseEntity mockGetMyOffers(@RequestParam MultiValueMap<String, String> queryParams
        , UriComponentsBuilder uriBuilder) {

        log.debug("REST request to get mock myoffers");
        //Returns mock for testing purposes

        Page<Offer> page = new PageImpl<>(matchingService.getMockOffers());
        HttpHeaders headers =
            PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    /**
     * {@code GET /my-offers} : get offers targeted at user
     *
     * @return the {@link ResponseEntity} with List of Offers {@code 200}
     */
    @GetMapping("/my-offers")
    public ResponseEntity getMyOffers(Principal principal) throws IllegalAccessException {

        log.debug("REST request to get my offers");

        User user = userRepository.findOneByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("Principal name not found in DB"));

        return ResponseEntity.ok(matchingService.getOffers(user));
    }


    @PostMapping("/offers/create-full")
    public ResponseEntity creteFullOffer(@RequestBody FullOfferDTO fullOfferDTO, Principal principal) {
        User user = userRepository.findOneByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("Principal name not found in DB"));

        Offer offer = setUpOfferFromDTO(fullOfferDTO, user);

        return ResponseEntity.ok(offer);
    }

    private Offer setUpOfferFromDTO(@RequestBody FullOfferDTO fullOfferDTO, User user) {
        Offer offer = new Offer();
        offer.setRoomAmount(fullOfferDTO.getOffer().getRoomAmount());
        offer.setSize(fullOfferDTO.getOffer().getSize());
        offer.setDescription(fullOfferDTO.getOffer().getDescription());
        offer.setTotalCost(fullOfferDTO.getOffer().getTotalCost());
        offer.setConstructionYear(fullOfferDTO.getOffer().getConstructionYear());
        offer.setIsFurnished(fullOfferDTO.getOffer().isIsFurnished());
        offer.setPets(fullOfferDTO.getOffer().isPets());
        offer.setSmokingInside(fullOfferDTO.getOffer().isSmokingInside());
        offer.setAlbum(fullOfferDTO.getAlbum());
        offer.setAddress(fullOfferDTO.getAddress());
        offer.setUser(user);
        log.debug(String.format("Created offer %s", fullOfferDTO.toString()));
        return offer;
    }


    /**
     * Returns top 3 offers in city where user is located.
     * User has to fill questionnaire first
     *
     * @param principal
     * @return
     */
    @GetMapping("/offers/get-top-3")
    public ResponseEntity getTopThree(Principal principal) {

        User user = userRepository.findOneByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("Principal name not found in DB"));

        return ResponseEntity.ok(matchingService.getTopViewedOffersInUsersCity(user));
    }


}
