package com.flatter.server.web.rest;

import com.flatter.server.FlatterservermonolithApp;
import com.flatter.server.domain.ProfilePicture;
import com.flatter.server.repository.ProfilePictureRepository;
import com.flatter.server.repository.UserRepository;
import com.flatter.server.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.flatter.server.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProfilePictureResource REST controller.
 *
 * @see ProfilePictureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlatterservermonolithApp.class)
public class ProfilePictureResourceIntTest {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final Instant DEFAULT_TAKEN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TAKEN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPLOADED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOADED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    @Autowired
    private UserRepository userRepository;

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

    private MockMvc restProfilePictureMockMvc;

    private ProfilePicture profilePicture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfilePictureResource profilePictureResource = new ProfilePictureResource(profilePictureRepository, userRepository);
        this.restProfilePictureMockMvc = MockMvcBuilders.standaloneSetup(profilePictureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfilePicture createEntity(EntityManager em) {
        ProfilePicture profilePicture = new ProfilePicture()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .height(DEFAULT_HEIGHT)
            .width(DEFAULT_WIDTH)
            .taken(DEFAULT_TAKEN)
            .uploaded(DEFAULT_UPLOADED);
        return profilePicture;
    }

    @Before
    public void initTest() {
        profilePicture = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfilePicture() throws Exception {
        int databaseSizeBeforeCreate = profilePictureRepository.findAll().size();

        // Create the ProfilePicture
        restProfilePictureMockMvc.perform(post("/api/profile-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profilePicture)))
            .andExpect(status().isCreated());

        // Validate the ProfilePicture in the database
        List<ProfilePicture> profilePictureList = profilePictureRepository.findAll();
        assertThat(profilePictureList).hasSize(databaseSizeBeforeCreate + 1);
        ProfilePicture testProfilePicture = profilePictureList.get(profilePictureList.size() - 1);
        assertThat(testProfilePicture.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProfilePicture.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProfilePicture.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testProfilePicture.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testProfilePicture.getTaken()).isEqualTo(DEFAULT_TAKEN);
        assertThat(testProfilePicture.getUploaded()).isEqualTo(DEFAULT_UPLOADED);
    }

    @Test
    @Transactional
    public void createProfilePictureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profilePictureRepository.findAll().size();

        // Create the ProfilePicture with an existing ID
        profilePicture.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfilePictureMockMvc.perform(post("/api/profile-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profilePicture)))
            .andExpect(status().isBadRequest());

        // Validate the ProfilePicture in the database
        List<ProfilePicture> profilePictureList = profilePictureRepository.findAll();
        assertThat(profilePictureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProfilePictures() throws Exception {
        // Initialize the database
        profilePictureRepository.saveAndFlush(profilePicture);

        // Get all the profilePictureList
        restProfilePictureMockMvc.perform(get("/api/profile-pictures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profilePicture.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].taken").value(hasItem(DEFAULT_TAKEN.toString())))
            .andExpect(jsonPath("$.[*].uploaded").value(hasItem(DEFAULT_UPLOADED.toString())));
    }

    @Test
    @Transactional
    public void getProfilePicture() throws Exception {
        // Initialize the database
        profilePictureRepository.saveAndFlush(profilePicture);

        // Get the profilePicture
        restProfilePictureMockMvc.perform(get("/api/profile-pictures/{id}", profilePicture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profilePicture.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.taken").value(DEFAULT_TAKEN.toString()))
            .andExpect(jsonPath("$.uploaded").value(DEFAULT_UPLOADED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProfilePicture() throws Exception {
        // Get the profilePicture
        restProfilePictureMockMvc.perform(get("/api/profile-pictures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfilePicture() throws Exception {
        // Initialize the database
        profilePictureRepository.saveAndFlush(profilePicture);

        int databaseSizeBeforeUpdate = profilePictureRepository.findAll().size();

        // Update the profilePicture
        ProfilePicture updatedProfilePicture = profilePictureRepository.findById(profilePicture.getId()).get();
        // Disconnect from session so that the updates on updatedProfilePicture are not directly saved in db
        em.detach(updatedProfilePicture);
        updatedProfilePicture
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .taken(UPDATED_TAKEN)
            .uploaded(UPDATED_UPLOADED);

        restProfilePictureMockMvc.perform(put("/api/profile-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfilePicture)))
            .andExpect(status().isOk());

        // Validate the ProfilePicture in the database
        List<ProfilePicture> profilePictureList = profilePictureRepository.findAll();
        assertThat(profilePictureList).hasSize(databaseSizeBeforeUpdate);
        ProfilePicture testProfilePicture = profilePictureList.get(profilePictureList.size() - 1);
        assertThat(testProfilePicture.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProfilePicture.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProfilePicture.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testProfilePicture.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testProfilePicture.getTaken()).isEqualTo(UPDATED_TAKEN);
        assertThat(testProfilePicture.getUploaded()).isEqualTo(UPDATED_UPLOADED);
    }

    @Test
    @Transactional
    public void updateNonExistingProfilePicture() throws Exception {
        int databaseSizeBeforeUpdate = profilePictureRepository.findAll().size();

        // Create the ProfilePicture

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilePictureMockMvc.perform(put("/api/profile-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profilePicture)))
            .andExpect(status().isBadRequest());

        // Validate the ProfilePicture in the database
        List<ProfilePicture> profilePictureList = profilePictureRepository.findAll();
        assertThat(profilePictureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfilePicture() throws Exception {
        // Initialize the database
        profilePictureRepository.saveAndFlush(profilePicture);

        int databaseSizeBeforeDelete = profilePictureRepository.findAll().size();

        // Delete the profilePicture
        restProfilePictureMockMvc.perform(delete("/api/profile-pictures/{id}", profilePicture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProfilePicture> profilePictureList = profilePictureRepository.findAll();
        assertThat(profilePictureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfilePicture.class);
        ProfilePicture profilePicture1 = new ProfilePicture();
        profilePicture1.setId(1L);
        ProfilePicture profilePicture2 = new ProfilePicture();
        profilePicture2.setId(profilePicture1.getId());
        assertThat(profilePicture1).isEqualTo(profilePicture2);
        profilePicture2.setId(2L);
        assertThat(profilePicture1).isNotEqualTo(profilePicture2);
        profilePicture1.setId(null);
        assertThat(profilePicture1).isNotEqualTo(profilePicture2);
    }
}
