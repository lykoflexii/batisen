package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.Entreprise;
import com.gesbtp.atos.repository.EntrepriseRepository;
import com.gesbtp.atos.service.EntrepriseService;
import com.gesbtp.atos.repository.search.EntrepriseSearchRepository;
import com.gesbtp.atos.service.dto.EntrepriseDTO;
import com.gesbtp.atos.service.mapper.EntrepriseMapper;
import com.gesbtp.atos.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;

import static com.gesbtp.atos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EntrepriseResource REST controller.
 *
 * @see EntrepriseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class EntrepriseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMERCIAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMMERCIAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SIREN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SIREN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ENTREPRISE_ADMIN_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_ENTREPRISE_ADMIN_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_TELEPHONE = 1;
    private static final Integer UPDATED_TELEPHONE = 2;

    private static final Integer DEFAULT_PACK = 1;
    private static final Integer UPDATED_PACK = 2;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private EntrepriseMapper entrepriseMapper;

    @Autowired
    private EntrepriseService entrepriseService;

    @Autowired
    private EntrepriseSearchRepository entrepriseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntrepriseMockMvc;

    private Entreprise entreprise;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EntrepriseResource entrepriseResource = new EntrepriseResource(entrepriseService);
        this.restEntrepriseMockMvc = MockMvcBuilders.standaloneSetup(entrepriseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entreprise createEntity(EntityManager em) {
        Entreprise entreprise = new Entreprise()
            .name(DEFAULT_NAME)
            .commercialName(DEFAULT_COMMERCIAL_NAME)
            .sirenNumber(DEFAULT_SIREN_NUMBER)
            .entrepriseAdminEmail(DEFAULT_ENTREPRISE_ADMIN_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .pack(DEFAULT_PACK);
        return entreprise;
    }

    @Before
    public void initTest() {
        entrepriseSearchRepository.deleteAll();
        entreprise = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntreprise() throws Exception {
        int databaseSizeBeforeCreate = entrepriseRepository.findAll().size();

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);
        restEntrepriseMockMvc.perform(post("/api/entreprises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isCreated());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeCreate + 1);
        Entreprise testEntreprise = entrepriseList.get(entrepriseList.size() - 1);
        assertThat(testEntreprise.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEntreprise.getCommercialName()).isEqualTo(DEFAULT_COMMERCIAL_NAME);
        assertThat(testEntreprise.getSirenNumber()).isEqualTo(DEFAULT_SIREN_NUMBER);
        assertThat(testEntreprise.getEntrepriseAdminEmail()).isEqualTo(DEFAULT_ENTREPRISE_ADMIN_EMAIL);
        assertThat(testEntreprise.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testEntreprise.getPack()).isEqualTo(DEFAULT_PACK);

        // Validate the Entreprise in Elasticsearch
        Entreprise entrepriseEs = entrepriseSearchRepository.findOne(testEntreprise.getId());
        assertThat(entrepriseEs).isEqualToIgnoringGivenFields(testEntreprise);
    }

    @Test
    @Transactional
    public void createEntrepriseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entrepriseRepository.findAll().size();

        // Create the Entreprise with an existing ID
        entreprise.setId(1L);
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntrepriseMockMvc.perform(post("/api/entreprises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = entrepriseRepository.findAll().size();
        // set the field null
        entreprise.setName(null);

        // Create the Entreprise, which fails.
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        restEntrepriseMockMvc.perform(post("/api/entreprises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCommercialNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = entrepriseRepository.findAll().size();
        // set the field null
        entreprise.setCommercialName(null);

        // Create the Entreprise, which fails.
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        restEntrepriseMockMvc.perform(post("/api/entreprises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSirenNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = entrepriseRepository.findAll().size();
        // set the field null
        entreprise.setSirenNumber(null);

        // Create the Entreprise, which fails.
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        restEntrepriseMockMvc.perform(post("/api/entreprises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEntrepriseAdminEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = entrepriseRepository.findAll().size();
        // set the field null
        entreprise.setEntrepriseAdminEmail(null);

        // Create the Entreprise, which fails.
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        restEntrepriseMockMvc.perform(post("/api/entreprises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEntreprises() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList
        restEntrepriseMockMvc.perform(get("/api/entreprises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entreprise.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].commercialName").value(hasItem(DEFAULT_COMMERCIAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].sirenNumber").value(hasItem(DEFAULT_SIREN_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].entrepriseAdminEmail").value(hasItem(DEFAULT_ENTREPRISE_ADMIN_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].pack").value(hasItem(DEFAULT_PACK)));
    }

    @Test
    @Transactional
    public void getEntreprise() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);

        // Get the entreprise
        restEntrepriseMockMvc.perform(get("/api/entreprises/{id}", entreprise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entreprise.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.commercialName").value(DEFAULT_COMMERCIAL_NAME.toString()))
            .andExpect(jsonPath("$.sirenNumber").value(DEFAULT_SIREN_NUMBER.toString()))
            .andExpect(jsonPath("$.entrepriseAdminEmail").value(DEFAULT_ENTREPRISE_ADMIN_EMAIL.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.pack").value(DEFAULT_PACK));
    }

    @Test
    @Transactional
    public void getNonExistingEntreprise() throws Exception {
        // Get the entreprise
        restEntrepriseMockMvc.perform(get("/api/entreprises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntreprise() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);
        entrepriseSearchRepository.save(entreprise);
        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();

        // Update the entreprise
        Entreprise updatedEntreprise = entrepriseRepository.findOne(entreprise.getId());
        // Disconnect from session so that the updates on updatedEntreprise are not directly saved in db
        em.detach(updatedEntreprise);
        updatedEntreprise
            .name(UPDATED_NAME)
            .commercialName(UPDATED_COMMERCIAL_NAME)
            .sirenNumber(UPDATED_SIREN_NUMBER)
            .entrepriseAdminEmail(UPDATED_ENTREPRISE_ADMIN_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .pack(UPDATED_PACK);
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(updatedEntreprise);

        restEntrepriseMockMvc.perform(put("/api/entreprises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isOk());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
        Entreprise testEntreprise = entrepriseList.get(entrepriseList.size() - 1);
        assertThat(testEntreprise.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEntreprise.getCommercialName()).isEqualTo(UPDATED_COMMERCIAL_NAME);
        assertThat(testEntreprise.getSirenNumber()).isEqualTo(UPDATED_SIREN_NUMBER);
        assertThat(testEntreprise.getEntrepriseAdminEmail()).isEqualTo(UPDATED_ENTREPRISE_ADMIN_EMAIL);
        assertThat(testEntreprise.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEntreprise.getPack()).isEqualTo(UPDATED_PACK);

        // Validate the Entreprise in Elasticsearch
        Entreprise entrepriseEs = entrepriseSearchRepository.findOne(testEntreprise.getId());
        assertThat(entrepriseEs).isEqualToIgnoringGivenFields(testEntreprise);
    }

    @Test
    @Transactional
    public void updateNonExistingEntreprise() throws Exception {
        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntrepriseMockMvc.perform(put("/api/entreprises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isCreated());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntreprise() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);
        entrepriseSearchRepository.save(entreprise);
        int databaseSizeBeforeDelete = entrepriseRepository.findAll().size();

        // Get the entreprise
        restEntrepriseMockMvc.perform(delete("/api/entreprises/{id}", entreprise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean entrepriseExistsInEs = entrepriseSearchRepository.exists(entreprise.getId());
        assertThat(entrepriseExistsInEs).isFalse();

        // Validate the database is empty
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEntreprise() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);
        entrepriseSearchRepository.save(entreprise);

        // Search the entreprise
        restEntrepriseMockMvc.perform(get("/api/_search/entreprises?query=id:" + entreprise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entreprise.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].commercialName").value(hasItem(DEFAULT_COMMERCIAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].sirenNumber").value(hasItem(DEFAULT_SIREN_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].entrepriseAdminEmail").value(hasItem(DEFAULT_ENTREPRISE_ADMIN_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].pack").value(hasItem(DEFAULT_PACK)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entreprise.class);
        Entreprise entreprise1 = new Entreprise();
        entreprise1.setId(1L);
        Entreprise entreprise2 = new Entreprise();
        entreprise2.setId(entreprise1.getId());
        assertThat(entreprise1).isEqualTo(entreprise2);
        entreprise2.setId(2L);
        assertThat(entreprise1).isNotEqualTo(entreprise2);
        entreprise1.setId(null);
        assertThat(entreprise1).isNotEqualTo(entreprise2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntrepriseDTO.class);
        EntrepriseDTO entrepriseDTO1 = new EntrepriseDTO();
        entrepriseDTO1.setId(1L);
        EntrepriseDTO entrepriseDTO2 = new EntrepriseDTO();
        assertThat(entrepriseDTO1).isNotEqualTo(entrepriseDTO2);
        entrepriseDTO2.setId(entrepriseDTO1.getId());
        assertThat(entrepriseDTO1).isEqualTo(entrepriseDTO2);
        entrepriseDTO2.setId(2L);
        assertThat(entrepriseDTO1).isNotEqualTo(entrepriseDTO2);
        entrepriseDTO1.setId(null);
        assertThat(entrepriseDTO1).isNotEqualTo(entrepriseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(entrepriseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(entrepriseMapper.fromId(null)).isNull();
    }
}
