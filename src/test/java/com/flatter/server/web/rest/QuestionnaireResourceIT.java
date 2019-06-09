package com.flatter.server.web.rest;

import com.flatter.server.FlatterservermonolithApp;
import com.flatter.server.domain.Questionnaire;
import com.flatter.server.repository.QuestionnaireRepository;
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

/**
 * Integration tests for the {@Link QuestionnaireResource} REST controller.
 */
@SpringBootTest(classes = FlatterservermonolithApp.class)
public class QuestionnaireResourceIT {

    private static final Boolean DEFAULT_PETS = false;
    private static final Boolean UPDATED_PETS = true;

    private static final Boolean DEFAULT_SMOKING_INSIDE = false;
    private static final Boolean UPDATED_SMOKING_INSIDE = true;

    private static final Boolean DEFAULT_IS_FURNISHED = false;
    private static final Boolean UPDATED_IS_FURNISHED = true;

    private static final Integer DEFAULT_ROOM_AMOUNT_MIN = 1;
    private static final Integer UPDATED_ROOM_AMOUNT_MIN = 2;

    private static final Integer DEFAULT_ROOM_AMOUNT_MAX = 1;
    private static final Integer UPDATED_ROOM_AMOUNT_MAX = 2;

    private static final Double DEFAULT_SIZE_MIN = 1D;
    private static final Double UPDATED_SIZE_MIN = 2D;

    private static final Double DEFAULT_SIZE_MAX = 1D;
    private static final Double UPDATED_SIZE_MAX = 2D;

    private static final Integer DEFAULT_CONSTRUCTION_YEAR_MIN = 1;
    private static final Integer UPDATED_CONSTRUCTION_YEAR_MIN = 2;

    private static final Integer DEFAULT_CONSTRUCTION_YEAR_MAX = 1;
    private static final Integer UPDATED_CONSTRUCTION_YEAR_MAX = 2;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_COST_MIN = 1D;
    private static final Double UPDATED_TOTAL_COST_MIN = 2D;

    private static final Double DEFAULT_TOTAL_COST_MAX = 1D;
    private static final Double UPDATED_TOTAL_COST_MAX = 2D;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restQuestionnaireMockMvc;

    private Questionnaire questionnaire;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionnaireResource questionnaireResource = new QuestionnaireResource(questionnaireRepository);
        this.restQuestionnaireMockMvc = MockMvcBuilders.standaloneSetup(questionnaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionnaire createEntity(EntityManager em) {
        Questionnaire questionnaire = new Questionnaire()
            .pets(DEFAULT_PETS)
            .smokingInside(DEFAULT_SMOKING_INSIDE)
            .isFurnished(DEFAULT_IS_FURNISHED)
            .roomAmountMin(DEFAULT_ROOM_AMOUNT_MIN)
            .roomAmountMax(DEFAULT_ROOM_AMOUNT_MAX)
            .sizeMin(DEFAULT_SIZE_MIN)
            .sizeMax(DEFAULT_SIZE_MAX)
            .constructionYearMin(DEFAULT_CONSTRUCTION_YEAR_MIN)
            .constructionYearMax(DEFAULT_CONSTRUCTION_YEAR_MAX)
            .type(DEFAULT_TYPE)
            .totalCostMin(DEFAULT_TOTAL_COST_MIN)
            .totalCostMax(DEFAULT_TOTAL_COST_MAX)
            .city(DEFAULT_CITY);
        return questionnaire;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionnaire createUpdatedEntity(EntityManager em) {
        Questionnaire questionnaire = new Questionnaire()
            .pets(UPDATED_PETS)
            .smokingInside(UPDATED_SMOKING_INSIDE)
            .isFurnished(UPDATED_IS_FURNISHED)
            .roomAmountMin(UPDATED_ROOM_AMOUNT_MIN)
            .roomAmountMax(UPDATED_ROOM_AMOUNT_MAX)
            .sizeMin(UPDATED_SIZE_MIN)
            .sizeMax(UPDATED_SIZE_MAX)
            .constructionYearMin(UPDATED_CONSTRUCTION_YEAR_MIN)
            .constructionYearMax(UPDATED_CONSTRUCTION_YEAR_MAX)
            .type(UPDATED_TYPE)
            .totalCostMin(UPDATED_TOTAL_COST_MIN)
            .totalCostMax(UPDATED_TOTAL_COST_MAX)
            .city(UPDATED_CITY);
        return questionnaire;
    }

    @BeforeEach
    public void initTest() {
        questionnaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestionnaire() throws Exception {
        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();

        // Create the Questionnaire
        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isCreated());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate + 1);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.isPets()).isEqualTo(DEFAULT_PETS);
        assertThat(testQuestionnaire.isSmokingInside()).isEqualTo(DEFAULT_SMOKING_INSIDE);
        assertThat(testQuestionnaire.isIsFurnished()).isEqualTo(DEFAULT_IS_FURNISHED);
        assertThat(testQuestionnaire.getRoomAmountMin()).isEqualTo(DEFAULT_ROOM_AMOUNT_MIN);
        assertThat(testQuestionnaire.getRoomAmountMax()).isEqualTo(DEFAULT_ROOM_AMOUNT_MAX);
        assertThat(testQuestionnaire.getSizeMin()).isEqualTo(DEFAULT_SIZE_MIN);
        assertThat(testQuestionnaire.getSizeMax()).isEqualTo(DEFAULT_SIZE_MAX);
        assertThat(testQuestionnaire.getConstructionYearMin()).isEqualTo(DEFAULT_CONSTRUCTION_YEAR_MIN);
        assertThat(testQuestionnaire.getConstructionYearMax()).isEqualTo(DEFAULT_CONSTRUCTION_YEAR_MAX);
        assertThat(testQuestionnaire.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuestionnaire.getTotalCostMin()).isEqualTo(DEFAULT_TOTAL_COST_MIN);
        assertThat(testQuestionnaire.getTotalCostMax()).isEqualTo(DEFAULT_TOTAL_COST_MAX);
        assertThat(testQuestionnaire.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    public void createQuestionnaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();

        // Create the Questionnaire with an existing ID
        questionnaire.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionnaireRepository.findAll().size();
        // set the field null
        questionnaire.setCity(null);

        // Create the Questionnaire, which fails.

        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isBadRequest());

        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestionnaires() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList
        restQuestionnaireMockMvc.perform(get("/api/questionnaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].pets").value(hasItem(DEFAULT_PETS.booleanValue())))
            .andExpect(jsonPath("$.[*].smokingInside").value(hasItem(DEFAULT_SMOKING_INSIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].isFurnished").value(hasItem(DEFAULT_IS_FURNISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].roomAmountMin").value(hasItem(DEFAULT_ROOM_AMOUNT_MIN)))
            .andExpect(jsonPath("$.[*].roomAmountMax").value(hasItem(DEFAULT_ROOM_AMOUNT_MAX)))
            .andExpect(jsonPath("$.[*].sizeMin").value(hasItem(DEFAULT_SIZE_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].sizeMax").value(hasItem(DEFAULT_SIZE_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].constructionYearMin").value(hasItem(DEFAULT_CONSTRUCTION_YEAR_MIN)))
            .andExpect(jsonPath("$.[*].constructionYearMax").value(hasItem(DEFAULT_CONSTRUCTION_YEAR_MAX)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalCostMin").value(hasItem(DEFAULT_TOTAL_COST_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].totalCostMax").value(hasItem(DEFAULT_TOTAL_COST_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())));
    }
    
    @Test
    @Transactional
    public void getQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/{id}", questionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(questionnaire.getId().intValue()))
            .andExpect(jsonPath("$.pets").value(DEFAULT_PETS.booleanValue()))
            .andExpect(jsonPath("$.smokingInside").value(DEFAULT_SMOKING_INSIDE.booleanValue()))
            .andExpect(jsonPath("$.isFurnished").value(DEFAULT_IS_FURNISHED.booleanValue()))
            .andExpect(jsonPath("$.roomAmountMin").value(DEFAULT_ROOM_AMOUNT_MIN))
            .andExpect(jsonPath("$.roomAmountMax").value(DEFAULT_ROOM_AMOUNT_MAX))
            .andExpect(jsonPath("$.sizeMin").value(DEFAULT_SIZE_MIN.doubleValue()))
            .andExpect(jsonPath("$.sizeMax").value(DEFAULT_SIZE_MAX.doubleValue()))
            .andExpect(jsonPath("$.constructionYearMin").value(DEFAULT_CONSTRUCTION_YEAR_MIN))
            .andExpect(jsonPath("$.constructionYearMax").value(DEFAULT_CONSTRUCTION_YEAR_MAX))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.totalCostMin").value(DEFAULT_TOTAL_COST_MIN.doubleValue()))
            .andExpect(jsonPath("$.totalCostMax").value(DEFAULT_TOTAL_COST_MAX.doubleValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionnaire() throws Exception {
        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire
        Questionnaire updatedQuestionnaire = questionnaireRepository.findById(questionnaire.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionnaire are not directly saved in db
        em.detach(updatedQuestionnaire);
        updatedQuestionnaire
            .pets(UPDATED_PETS)
            .smokingInside(UPDATED_SMOKING_INSIDE)
            .isFurnished(UPDATED_IS_FURNISHED)
            .roomAmountMin(UPDATED_ROOM_AMOUNT_MIN)
            .roomAmountMax(UPDATED_ROOM_AMOUNT_MAX)
            .sizeMin(UPDATED_SIZE_MIN)
            .sizeMax(UPDATED_SIZE_MAX)
            .constructionYearMin(UPDATED_CONSTRUCTION_YEAR_MIN)
            .constructionYearMax(UPDATED_CONSTRUCTION_YEAR_MAX)
            .type(UPDATED_TYPE)
            .totalCostMin(UPDATED_TOTAL_COST_MIN)
            .totalCostMax(UPDATED_TOTAL_COST_MAX)
            .city(UPDATED_CITY);

        restQuestionnaireMockMvc.perform(put("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestionnaire)))
            .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.isPets()).isEqualTo(UPDATED_PETS);
        assertThat(testQuestionnaire.isSmokingInside()).isEqualTo(UPDATED_SMOKING_INSIDE);
        assertThat(testQuestionnaire.isIsFurnished()).isEqualTo(UPDATED_IS_FURNISHED);
        assertThat(testQuestionnaire.getRoomAmountMin()).isEqualTo(UPDATED_ROOM_AMOUNT_MIN);
        assertThat(testQuestionnaire.getRoomAmountMax()).isEqualTo(UPDATED_ROOM_AMOUNT_MAX);
        assertThat(testQuestionnaire.getSizeMin()).isEqualTo(UPDATED_SIZE_MIN);
        assertThat(testQuestionnaire.getSizeMax()).isEqualTo(UPDATED_SIZE_MAX);
        assertThat(testQuestionnaire.getConstructionYearMin()).isEqualTo(UPDATED_CONSTRUCTION_YEAR_MIN);
        assertThat(testQuestionnaire.getConstructionYearMax()).isEqualTo(UPDATED_CONSTRUCTION_YEAR_MAX);
        assertThat(testQuestionnaire.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testQuestionnaire.getTotalCostMin()).isEqualTo(UPDATED_TOTAL_COST_MIN);
        assertThat(testQuestionnaire.getTotalCostMax()).isEqualTo(UPDATED_TOTAL_COST_MAX);
        assertThat(testQuestionnaire.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Create the Questionnaire

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc.perform(put("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeDelete = questionnaireRepository.findAll().size();

        // Delete the questionnaire
        restQuestionnaireMockMvc.perform(delete("/api/questionnaires/{id}", questionnaire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Questionnaire.class);
        Questionnaire questionnaire1 = new Questionnaire();
        questionnaire1.setId(1L);
        Questionnaire questionnaire2 = new Questionnaire();
        questionnaire2.setId(questionnaire1.getId());
        assertThat(questionnaire1).isEqualTo(questionnaire2);
        questionnaire2.setId(2L);
        assertThat(questionnaire1).isNotEqualTo(questionnaire2);
        questionnaire1.setId(null);
        assertThat(questionnaire1).isNotEqualTo(questionnaire2);
    }
}
