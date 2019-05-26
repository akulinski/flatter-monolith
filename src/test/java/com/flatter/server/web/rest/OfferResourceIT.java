package com.flatter.server.web.rest;

import com.flatter.server.FlatterservermonolithApp;
import com.flatter.server.domain.Offer;
import com.flatter.server.repository.OfferRepository;
import com.flatter.server.repository.PhotoRepository;
import com.flatter.server.service.MatchingService;
import com.flatter.server.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.flatter.server.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Integration tests for the {@Link OfferResource} REST controller. */
@SpringBootTest(classes = FlatterservermonolithApp.class)
public class OfferResourceIT {

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final Double DEFAULT_TOTAL_COST = 1D;
  private static final Double UPDATED_TOTAL_COST = 2D;

  private static final Integer DEFAULT_ROOM_AMOUNT = 1;
  private static final Integer UPDATED_ROOM_AMOUNT = 2;

  private static final Double DEFAULT_SIZE = 1D;
  private static final Double UPDATED_SIZE = 2D;

  private static final String DEFAULT_TYPE = "AAAAAAAAAA";
  private static final String UPDATED_TYPE = "BBBBBBBBBB";

  private static final Integer DEFAULT_CONSTRUCTION_YEAR = 1;
  private static final Integer UPDATED_CONSTRUCTION_YEAR = 2;

  private static final Boolean DEFAULT_PETS = false;
  private static final Boolean UPDATED_PETS = true;

  private static final Boolean DEFAULT_SMOKING_INSIDE = false;
  private static final Boolean UPDATED_SMOKING_INSIDE = true;

  private static final Boolean DEFAULT_IS_FURNISHED = false;
  private static final Boolean UPDATED_IS_FURNISHED = true;

  @Autowired private OfferRepository offerRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restOfferMockMvc;

  private Offer offer;

  @Autowired private PhotoRepository photoRepository;

  @Autowired private MatchingService matchingService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final OfferResource offerResource =
        new OfferResource(offerRepository, photoRepository, matchingService);
    this.restOfferMockMvc =
        MockMvcBuilders.standaloneSetup(offerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator)
            .build();
  }

  /**
   * Create an entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Offer createEntity(EntityManager em) {
    Offer offer =
        new Offer()
            .description(DEFAULT_DESCRIPTION)
            .totalCost(DEFAULT_TOTAL_COST)
            .roomAmount(DEFAULT_ROOM_AMOUNT)
            .size(DEFAULT_SIZE)
            .type(DEFAULT_TYPE)
            .constructionYear(DEFAULT_CONSTRUCTION_YEAR)
            .pets(DEFAULT_PETS)
            .smokingInside(DEFAULT_SMOKING_INSIDE)
            .isFurnished(DEFAULT_IS_FURNISHED);
    return offer;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Offer createUpdatedEntity(EntityManager em) {
    Offer offer =
        new Offer()
            .description(UPDATED_DESCRIPTION)
            .totalCost(UPDATED_TOTAL_COST)
            .roomAmount(UPDATED_ROOM_AMOUNT)
            .size(UPDATED_SIZE)
            .type(UPDATED_TYPE)
            .constructionYear(UPDATED_CONSTRUCTION_YEAR)
            .pets(UPDATED_PETS)
            .smokingInside(UPDATED_SMOKING_INSIDE)
            .isFurnished(UPDATED_IS_FURNISHED);
    return offer;
  }

  @BeforeEach
  public void initTest() {
    offer = createEntity(em);
  }

  @Test
  @Transactional
  public void createOffer() throws Exception {
    int databaseSizeBeforeCreate = offerRepository.findAll().size();

    // Create the Offer
    restOfferMockMvc
        .perform(
            post("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
        .andExpect(status().isCreated());

    // Validate the Offer in the database
    List<Offer> offerList = offerRepository.findAll();
    assertThat(offerList).hasSize(databaseSizeBeforeCreate + 1);
    Offer testOffer = offerList.get(offerList.size() - 1);
    assertThat(testOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testOffer.getTotalCost()).isEqualTo(DEFAULT_TOTAL_COST);
    assertThat(testOffer.getRoomAmount()).isEqualTo(DEFAULT_ROOM_AMOUNT);
    assertThat(testOffer.getSize()).isEqualTo(DEFAULT_SIZE);
    assertThat(testOffer.getType()).isEqualTo(DEFAULT_TYPE);
    assertThat(testOffer.getConstructionYear()).isEqualTo(DEFAULT_CONSTRUCTION_YEAR);
    assertThat(testOffer.isPets()).isEqualTo(DEFAULT_PETS);
    assertThat(testOffer.isSmokingInside()).isEqualTo(DEFAULT_SMOKING_INSIDE);
    assertThat(testOffer.isIsFurnished()).isEqualTo(DEFAULT_IS_FURNISHED);
  }

  @Test
  @Transactional
  public void createOfferWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = offerRepository.findAll().size();

    // Create the Offer with an existing ID
    offer.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restOfferMockMvc
        .perform(
            post("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
        .andExpect(status().isBadRequest());

    // Validate the Offer in the database
    List<Offer> offerList = offerRepository.findAll();
    assertThat(offerList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkTotalCostIsRequired() throws Exception {
    int databaseSizeBeforeTest = offerRepository.findAll().size();
    // set the field null
    offer.setTotalCost(null);

    // Create the Offer, which fails.

    restOfferMockMvc
        .perform(
            post("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
        .andExpect(status().isBadRequest());

    List<Offer> offerList = offerRepository.findAll();
    assertThat(offerList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkRoomAmountIsRequired() throws Exception {
    int databaseSizeBeforeTest = offerRepository.findAll().size();
    // set the field null
    offer.setRoomAmount(null);

    // Create the Offer, which fails.

    restOfferMockMvc
        .perform(
            post("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
        .andExpect(status().isBadRequest());

    List<Offer> offerList = offerRepository.findAll();
    assertThat(offerList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkSizeIsRequired() throws Exception {
    int databaseSizeBeforeTest = offerRepository.findAll().size();
    // set the field null
    offer.setSize(null);

    // Create the Offer, which fails.

    restOfferMockMvc
        .perform(
            post("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
        .andExpect(status().isBadRequest());

    List<Offer> offerList = offerRepository.findAll();
    assertThat(offerList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllOffers() throws Exception {
    // Initialize the database
    offerRepository.saveAndFlush(offer);

    // Get all the offerList
    restOfferMockMvc
        .perform(get("/api/offers?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(offer.getId().intValue())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
        .andExpect(jsonPath("$.[*].totalCost").value(hasItem(DEFAULT_TOTAL_COST.doubleValue())))
        .andExpect(jsonPath("$.[*].roomAmount").value(hasItem(DEFAULT_ROOM_AMOUNT)))
        .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.doubleValue())))
        .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
        .andExpect(jsonPath("$.[*].constructionYear").value(hasItem(DEFAULT_CONSTRUCTION_YEAR)))
        .andExpect(jsonPath("$.[*].pets").value(hasItem(DEFAULT_PETS.booleanValue())))
        .andExpect(
            jsonPath("$.[*].smokingInside").value(hasItem(DEFAULT_SMOKING_INSIDE.booleanValue())))
        .andExpect(
            jsonPath("$.[*].isFurnished").value(hasItem(DEFAULT_IS_FURNISHED.booleanValue())));
  }

  @Test
  @Transactional
  public void getOffer() throws Exception {
    // Initialize the database
    offerRepository.saveAndFlush(offer);

    // Get the offer
    restOfferMockMvc
        .perform(get("/api/offers/{id}", offer.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(offer.getId().intValue()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
        .andExpect(jsonPath("$.totalCost").value(DEFAULT_TOTAL_COST.doubleValue()))
        .andExpect(jsonPath("$.roomAmount").value(DEFAULT_ROOM_AMOUNT))
        .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.doubleValue()))
        .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
        .andExpect(jsonPath("$.constructionYear").value(DEFAULT_CONSTRUCTION_YEAR))
        .andExpect(jsonPath("$.pets").value(DEFAULT_PETS.booleanValue()))
        .andExpect(jsonPath("$.smokingInside").value(DEFAULT_SMOKING_INSIDE.booleanValue()))
        .andExpect(jsonPath("$.isFurnished").value(DEFAULT_IS_FURNISHED.booleanValue()));
  }

  @Test
  @Transactional
  public void getNonExistingOffer() throws Exception {
    // Get the offer
    restOfferMockMvc
        .perform(get("/api/offers/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateOffer() throws Exception {
    // Initialize the database
    offerRepository.saveAndFlush(offer);

    int databaseSizeBeforeUpdate = offerRepository.findAll().size();

    // Update the offer
    Offer updatedOffer = offerRepository.findById(offer.getId()).get();
    // Disconnect from session so that the updates on updatedOffer are not directly saved in db
    em.detach(updatedOffer);
    updatedOffer
        .description(UPDATED_DESCRIPTION)
        .totalCost(UPDATED_TOTAL_COST)
        .roomAmount(UPDATED_ROOM_AMOUNT)
        .size(UPDATED_SIZE)
        .type(UPDATED_TYPE)
        .constructionYear(UPDATED_CONSTRUCTION_YEAR)
        .pets(UPDATED_PETS)
        .smokingInside(UPDATED_SMOKING_INSIDE)
        .isFurnished(UPDATED_IS_FURNISHED);

    restOfferMockMvc
        .perform(
            put("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOffer)))
        .andExpect(status().isOk());

    // Validate the Offer in the database
    List<Offer> offerList = offerRepository.findAll();
    assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
    Offer testOffer = offerList.get(offerList.size() - 1);
    assertThat(testOffer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testOffer.getTotalCost()).isEqualTo(UPDATED_TOTAL_COST);
    assertThat(testOffer.getRoomAmount()).isEqualTo(UPDATED_ROOM_AMOUNT);
    assertThat(testOffer.getSize()).isEqualTo(UPDATED_SIZE);
    assertThat(testOffer.getType()).isEqualTo(UPDATED_TYPE);
    assertThat(testOffer.getConstructionYear()).isEqualTo(UPDATED_CONSTRUCTION_YEAR);
    assertThat(testOffer.isPets()).isEqualTo(UPDATED_PETS);
    assertThat(testOffer.isSmokingInside()).isEqualTo(UPDATED_SMOKING_INSIDE);
    assertThat(testOffer.isIsFurnished()).isEqualTo(UPDATED_IS_FURNISHED);
  }

  @Test
  @Transactional
  public void updateNonExistingOffer() throws Exception {
    int databaseSizeBeforeUpdate = offerRepository.findAll().size();

    // Create the Offer

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restOfferMockMvc
        .perform(
            put("/api/offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offer)))
        .andExpect(status().isBadRequest());

    // Validate the Offer in the database
    List<Offer> offerList = offerRepository.findAll();
    assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteOffer() throws Exception {
    // Initialize the database
    offerRepository.saveAndFlush(offer);

    int databaseSizeBeforeDelete = offerRepository.findAll().size();

    // Delete the offer
    restOfferMockMvc
        .perform(delete("/api/offers/{id}", offer.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database is empty
    List<Offer> offerList = offerRepository.findAll();
    assertThat(offerList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Offer.class);
    Offer offer1 = new Offer();
    offer1.setId(1L);
    Offer offer2 = new Offer();
    offer2.setId(offer1.getId());
    assertThat(offer1).isEqualTo(offer2);
    offer2.setId(2L);
    assertThat(offer1).isNotEqualTo(offer2);
    offer1.setId(null);
    assertThat(offer1).isNotEqualTo(offer2);
  }
}
