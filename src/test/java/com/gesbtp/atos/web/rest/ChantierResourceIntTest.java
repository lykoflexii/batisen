package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.Chantier;
import com.gesbtp.atos.repository.ChantierRepository;
import com.gesbtp.atos.service.ChantierService;
import com.gesbtp.atos.repository.search.ChantierSearchRepository;
import com.gesbtp.atos.service.dto.ChantierDTO;
import com.gesbtp.atos.service.mapper.ChantierMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.gesbtp.atos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gesbtp.atos.domain.enumeration.EtatChantier;
/**
 * Test class for the ChantierResource REST controller.
 *
 * @see ChantierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class ChantierResourceIntTest {

    private static final String DEFAULT_NOM_CHANTIER = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CHANTIER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_CHANTIER = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_CHANTIER = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final EtatChantier DEFAULT_ETAT_CHANTIER = EtatChantier.EN_COURS;
    private static final EtatChantier UPDATED_ETAT_CHANTIER = EtatChantier.TERMINER;

    private static final LocalDate DEFAULT_DATE_DEBUT_REELLE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_REELLE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN_REELLE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_REELLE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_DEBUT_PREVU = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_PREVU = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN_PREVU = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_PREVU = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ChantierRepository chantierRepository;

    @Autowired
    private ChantierMapper chantierMapper;

    @Autowired
    private ChantierService chantierService;

    @Autowired
    private ChantierSearchRepository chantierSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChantierMockMvc;

    private Chantier chantier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChantierResource chantierResource = new ChantierResource(chantierService);
        this.restChantierMockMvc = MockMvcBuilders.standaloneSetup(chantierResource)
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
    public static Chantier createEntity(EntityManager em) {
        Chantier chantier = new Chantier()
            .nomChantier(DEFAULT_NOM_CHANTIER)
            .descriptionChantier(DEFAULT_DESCRIPTION_CHANTIER)
            .ville(DEFAULT_VILLE)
            .adresse(DEFAULT_ADRESSE)
            .etatChantier(DEFAULT_ETAT_CHANTIER)
            .dateDebutReelle(DEFAULT_DATE_DEBUT_REELLE)
            .dateFinReelle(DEFAULT_DATE_FIN_REELLE)
            .dateDebutPrevu(DEFAULT_DATE_DEBUT_PREVU)
            .dateFinPrevu(DEFAULT_DATE_FIN_PREVU);
        return chantier;
    }

    @Before
    public void initTest() {
        chantierSearchRepository.deleteAll();
        chantier = createEntity(em);
    }

    @Test
    @Transactional
    public void createChantier() throws Exception {
        int databaseSizeBeforeCreate = chantierRepository.findAll().size();

        // Create the Chantier
        ChantierDTO chantierDTO = chantierMapper.toDto(chantier);
        restChantierMockMvc.perform(post("/api/chantiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
            .andExpect(status().isCreated());

        // Validate the Chantier in the database
        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeCreate + 1);
        Chantier testChantier = chantierList.get(chantierList.size() - 1);
        assertThat(testChantier.getNomChantier()).isEqualTo(DEFAULT_NOM_CHANTIER);
        assertThat(testChantier.getDescriptionChantier()).isEqualTo(DEFAULT_DESCRIPTION_CHANTIER);
        assertThat(testChantier.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testChantier.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testChantier.getEtatChantier()).isEqualTo(DEFAULT_ETAT_CHANTIER);
        assertThat(testChantier.getDateDebutReelle()).isEqualTo(DEFAULT_DATE_DEBUT_REELLE);
        assertThat(testChantier.getDateFinReelle()).isEqualTo(DEFAULT_DATE_FIN_REELLE);
        assertThat(testChantier.getDateDebutPrevu()).isEqualTo(DEFAULT_DATE_DEBUT_PREVU);
        assertThat(testChantier.getDateFinPrevu()).isEqualTo(DEFAULT_DATE_FIN_PREVU);

        // Validate the Chantier in Elasticsearch
        Chantier chantierEs = chantierSearchRepository.findOne(testChantier.getId());
        assertThat(chantierEs).isEqualToIgnoringGivenFields(testChantier);
    }

    @Test
    @Transactional
    public void createChantierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chantierRepository.findAll().size();

        // Create the Chantier with an existing ID
        chantier.setId(1L);
        ChantierDTO chantierDTO = chantierMapper.toDto(chantier);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChantierMockMvc.perform(post("/api/chantiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chantier in the database
        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomChantierIsRequired() throws Exception {
        int databaseSizeBeforeTest = chantierRepository.findAll().size();
        // set the field null
        chantier.setNomChantier(null);

        // Create the Chantier, which fails.
        ChantierDTO chantierDTO = chantierMapper.toDto(chantier);

        restChantierMockMvc.perform(post("/api/chantiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
            .andExpect(status().isBadRequest());

        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = chantierRepository.findAll().size();
        // set the field null
        chantier.setVille(null);

        // Create the Chantier, which fails.
        ChantierDTO chantierDTO = chantierMapper.toDto(chantier);

        restChantierMockMvc.perform(post("/api/chantiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
            .andExpect(status().isBadRequest());

        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = chantierRepository.findAll().size();
        // set the field null
        chantier.setAdresse(null);

        // Create the Chantier, which fails.
        ChantierDTO chantierDTO = chantierMapper.toDto(chantier);

        restChantierMockMvc.perform(post("/api/chantiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
            .andExpect(status().isBadRequest());

        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDebutPrevuIsRequired() throws Exception {
        int databaseSizeBeforeTest = chantierRepository.findAll().size();
        // set the field null
        chantier.setDateDebutPrevu(null);

        // Create the Chantier, which fails.
        ChantierDTO chantierDTO = chantierMapper.toDto(chantier);

        restChantierMockMvc.perform(post("/api/chantiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
            .andExpect(status().isBadRequest());

        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFinPrevuIsRequired() throws Exception {
        int databaseSizeBeforeTest = chantierRepository.findAll().size();
        // set the field null
        chantier.setDateFinPrevu(null);

        // Create the Chantier, which fails.
        ChantierDTO chantierDTO = chantierMapper.toDto(chantier);

        restChantierMockMvc.perform(post("/api/chantiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
            .andExpect(status().isBadRequest());

        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChantiers() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);

        // Get all the chantierList
        restChantierMockMvc.perform(get("/api/chantiers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chantier.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomChantier").value(hasItem(DEFAULT_NOM_CHANTIER.toString())))
            .andExpect(jsonPath("$.[*].descriptionChantier").value(hasItem(DEFAULT_DESCRIPTION_CHANTIER.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].etatChantier").value(hasItem(DEFAULT_ETAT_CHANTIER.toString())))
            .andExpect(jsonPath("$.[*].dateDebutReelle").value(hasItem(DEFAULT_DATE_DEBUT_REELLE.toString())))
            .andExpect(jsonPath("$.[*].dateFinReelle").value(hasItem(DEFAULT_DATE_FIN_REELLE.toString())))
            .andExpect(jsonPath("$.[*].dateDebutPrevu").value(hasItem(DEFAULT_DATE_DEBUT_PREVU.toString())))
            .andExpect(jsonPath("$.[*].dateFinPrevu").value(hasItem(DEFAULT_DATE_FIN_PREVU.toString())));
    }

    @Test
    @Transactional
    public void getChantier() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);

        // Get the chantier
        restChantierMockMvc.perform(get("/api/chantiers/{id}", chantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chantier.getId().intValue()))
            .andExpect(jsonPath("$.nomChantier").value(DEFAULT_NOM_CHANTIER.toString()))
            .andExpect(jsonPath("$.descriptionChantier").value(DEFAULT_DESCRIPTION_CHANTIER.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.etatChantier").value(DEFAULT_ETAT_CHANTIER.toString()))
            .andExpect(jsonPath("$.dateDebutReelle").value(DEFAULT_DATE_DEBUT_REELLE.toString()))
            .andExpect(jsonPath("$.dateFinReelle").value(DEFAULT_DATE_FIN_REELLE.toString()))
            .andExpect(jsonPath("$.dateDebutPrevu").value(DEFAULT_DATE_DEBUT_PREVU.toString()))
            .andExpect(jsonPath("$.dateFinPrevu").value(DEFAULT_DATE_FIN_PREVU.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChantier() throws Exception {
        // Get the chantier
        restChantierMockMvc.perform(get("/api/chantiers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChantier() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);
        chantierSearchRepository.save(chantier);
        int databaseSizeBeforeUpdate = chantierRepository.findAll().size();

        // Update the chantier
        Chantier updatedChantier = chantierRepository.findOne(chantier.getId());
        // Disconnect from session so that the updates on updatedChantier are not directly saved in db
        em.detach(updatedChantier);
        updatedChantier
            .nomChantier(UPDATED_NOM_CHANTIER)
            .descriptionChantier(UPDATED_DESCRIPTION_CHANTIER)
            .ville(UPDATED_VILLE)
            .adresse(UPDATED_ADRESSE)
            .etatChantier(UPDATED_ETAT_CHANTIER)
            .dateDebutReelle(UPDATED_DATE_DEBUT_REELLE)
            .dateFinReelle(UPDATED_DATE_FIN_REELLE)
            .dateDebutPrevu(UPDATED_DATE_DEBUT_PREVU)
            .dateFinPrevu(UPDATED_DATE_FIN_PREVU);
        ChantierDTO chantierDTO = chantierMapper.toDto(updatedChantier);

        restChantierMockMvc.perform(put("/api/chantiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
            .andExpect(status().isOk());

        // Validate the Chantier in the database
        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeUpdate);
        Chantier testChantier = chantierList.get(chantierList.size() - 1);
        assertThat(testChantier.getNomChantier()).isEqualTo(UPDATED_NOM_CHANTIER);
        assertThat(testChantier.getDescriptionChantier()).isEqualTo(UPDATED_DESCRIPTION_CHANTIER);
        assertThat(testChantier.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testChantier.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testChantier.getEtatChantier()).isEqualTo(UPDATED_ETAT_CHANTIER);
        assertThat(testChantier.getDateDebutReelle()).isEqualTo(UPDATED_DATE_DEBUT_REELLE);
        assertThat(testChantier.getDateFinReelle()).isEqualTo(UPDATED_DATE_FIN_REELLE);
        assertThat(testChantier.getDateDebutPrevu()).isEqualTo(UPDATED_DATE_DEBUT_PREVU);
        assertThat(testChantier.getDateFinPrevu()).isEqualTo(UPDATED_DATE_FIN_PREVU);

        // Validate the Chantier in Elasticsearch
        Chantier chantierEs = chantierSearchRepository.findOne(testChantier.getId());
        assertThat(chantierEs).isEqualToIgnoringGivenFields(testChantier);
    }

    @Test
    @Transactional
    public void updateNonExistingChantier() throws Exception {
        int databaseSizeBeforeUpdate = chantierRepository.findAll().size();

        // Create the Chantier
        ChantierDTO chantierDTO = chantierMapper.toDto(chantier);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChantierMockMvc.perform(put("/api/chantiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chantierDTO)))
            .andExpect(status().isCreated());

        // Validate the Chantier in the database
        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChantier() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);
        chantierSearchRepository.save(chantier);
        int databaseSizeBeforeDelete = chantierRepository.findAll().size();

        // Get the chantier
        restChantierMockMvc.perform(delete("/api/chantiers/{id}", chantier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean chantierExistsInEs = chantierSearchRepository.exists(chantier.getId());
        assertThat(chantierExistsInEs).isFalse();

        // Validate the database is empty
        List<Chantier> chantierList = chantierRepository.findAll();
        assertThat(chantierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChantier() throws Exception {
        // Initialize the database
        chantierRepository.saveAndFlush(chantier);
        chantierSearchRepository.save(chantier);

        // Search the chantier
        restChantierMockMvc.perform(get("/api/_search/chantiers?query=id:" + chantier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chantier.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomChantier").value(hasItem(DEFAULT_NOM_CHANTIER.toString())))
            .andExpect(jsonPath("$.[*].descriptionChantier").value(hasItem(DEFAULT_DESCRIPTION_CHANTIER.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].etatChantier").value(hasItem(DEFAULT_ETAT_CHANTIER.toString())))
            .andExpect(jsonPath("$.[*].dateDebutReelle").value(hasItem(DEFAULT_DATE_DEBUT_REELLE.toString())))
            .andExpect(jsonPath("$.[*].dateFinReelle").value(hasItem(DEFAULT_DATE_FIN_REELLE.toString())))
            .andExpect(jsonPath("$.[*].dateDebutPrevu").value(hasItem(DEFAULT_DATE_DEBUT_PREVU.toString())))
            .andExpect(jsonPath("$.[*].dateFinPrevu").value(hasItem(DEFAULT_DATE_FIN_PREVU.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chantier.class);
        Chantier chantier1 = new Chantier();
        chantier1.setId(1L);
        Chantier chantier2 = new Chantier();
        chantier2.setId(chantier1.getId());
        assertThat(chantier1).isEqualTo(chantier2);
        chantier2.setId(2L);
        assertThat(chantier1).isNotEqualTo(chantier2);
        chantier1.setId(null);
        assertThat(chantier1).isNotEqualTo(chantier2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChantierDTO.class);
        ChantierDTO chantierDTO1 = new ChantierDTO();
        chantierDTO1.setId(1L);
        ChantierDTO chantierDTO2 = new ChantierDTO();
        assertThat(chantierDTO1).isNotEqualTo(chantierDTO2);
        chantierDTO2.setId(chantierDTO1.getId());
        assertThat(chantierDTO1).isEqualTo(chantierDTO2);
        chantierDTO2.setId(2L);
        assertThat(chantierDTO1).isNotEqualTo(chantierDTO2);
        chantierDTO1.setId(null);
        assertThat(chantierDTO1).isNotEqualTo(chantierDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chantierMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chantierMapper.fromId(null)).isNull();
    }
}
