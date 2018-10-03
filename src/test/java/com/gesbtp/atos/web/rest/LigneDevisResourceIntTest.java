package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.LigneDevis;
import com.gesbtp.atos.repository.LigneDevisRepository;
import com.gesbtp.atos.service.LigneDevisService;
import com.gesbtp.atos.repository.search.LigneDevisSearchRepository;
import com.gesbtp.atos.service.dto.LigneDevisDTO;
import com.gesbtp.atos.service.mapper.LigneDevisMapper;
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
 * Test class for the LigneDevisResource REST controller.
 *
 * @see LigneDevisResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class LigneDevisResourceIntTest {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX_UNITAIRE = 1D;
    private static final Double UPDATED_PRIX_UNITAIRE = 2D;

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    @Autowired
    private LigneDevisRepository ligneDevisRepository;

    @Autowired
    private LigneDevisMapper ligneDevisMapper;

    @Autowired
    private LigneDevisService ligneDevisService;

    @Autowired
    private LigneDevisSearchRepository ligneDevisSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLigneDevisMockMvc;

    private LigneDevis ligneDevis;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LigneDevisResource ligneDevisResource = new LigneDevisResource(ligneDevisService);
        this.restLigneDevisMockMvc = MockMvcBuilders.standaloneSetup(ligneDevisResource)
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
    public static LigneDevis createEntity(EntityManager em) {
        LigneDevis ligneDevis = new LigneDevis()
            .designation(DEFAULT_DESIGNATION)
            .prixUnitaire(DEFAULT_PRIX_UNITAIRE)
            .quantite(DEFAULT_QUANTITE);
        return ligneDevis;
    }

    @Before
    public void initTest() {
        ligneDevisSearchRepository.deleteAll();
        ligneDevis = createEntity(em);
    }

    @Test
    @Transactional
    public void createLigneDevis() throws Exception {
        int databaseSizeBeforeCreate = ligneDevisRepository.findAll().size();

        // Create the LigneDevis
        LigneDevisDTO ligneDevisDTO = ligneDevisMapper.toDto(ligneDevis);
        restLigneDevisMockMvc.perform(post("/api/ligne-devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneDevisDTO)))
            .andExpect(status().isCreated());

        // Validate the LigneDevis in the database
        List<LigneDevis> ligneDevisList = ligneDevisRepository.findAll();
        assertThat(ligneDevisList).hasSize(databaseSizeBeforeCreate + 1);
        LigneDevis testLigneDevis = ligneDevisList.get(ligneDevisList.size() - 1);
        assertThat(testLigneDevis.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testLigneDevis.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testLigneDevis.getQuantite()).isEqualTo(DEFAULT_QUANTITE);

        // Validate the LigneDevis in Elasticsearch
        LigneDevis ligneDevisEs = ligneDevisSearchRepository.findOne(testLigneDevis.getId());
        assertThat(ligneDevisEs).isEqualToIgnoringGivenFields(testLigneDevis);
    }

    @Test
    @Transactional
    public void createLigneDevisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligneDevisRepository.findAll().size();

        // Create the LigneDevis with an existing ID
        ligneDevis.setId(1L);
        LigneDevisDTO ligneDevisDTO = ligneDevisMapper.toDto(ligneDevis);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneDevisMockMvc.perform(post("/api/ligne-devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneDevisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LigneDevis in the database
        List<LigneDevis> ligneDevisList = ligneDevisRepository.findAll();
        assertThat(ligneDevisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneDevisRepository.findAll().size();
        // set the field null
        ligneDevis.setDesignation(null);

        // Create the LigneDevis, which fails.
        LigneDevisDTO ligneDevisDTO = ligneDevisMapper.toDto(ligneDevis);

        restLigneDevisMockMvc.perform(post("/api/ligne-devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneDevisDTO)))
            .andExpect(status().isBadRequest());

        List<LigneDevis> ligneDevisList = ligneDevisRepository.findAll();
        assertThat(ligneDevisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixUnitaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneDevisRepository.findAll().size();
        // set the field null
        ligneDevis.setPrixUnitaire(null);

        // Create the LigneDevis, which fails.
        LigneDevisDTO ligneDevisDTO = ligneDevisMapper.toDto(ligneDevis);

        restLigneDevisMockMvc.perform(post("/api/ligne-devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneDevisDTO)))
            .andExpect(status().isBadRequest());

        List<LigneDevis> ligneDevisList = ligneDevisRepository.findAll();
        assertThat(ligneDevisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneDevisRepository.findAll().size();
        // set the field null
        ligneDevis.setQuantite(null);

        // Create the LigneDevis, which fails.
        LigneDevisDTO ligneDevisDTO = ligneDevisMapper.toDto(ligneDevis);

        restLigneDevisMockMvc.perform(post("/api/ligne-devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneDevisDTO)))
            .andExpect(status().isBadRequest());

        List<LigneDevis> ligneDevisList = ligneDevisRepository.findAll();
        assertThat(ligneDevisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLigneDevis() throws Exception {
        // Initialize the database
        ligneDevisRepository.saveAndFlush(ligneDevis);

        // Get all the ligneDevisList
        restLigneDevisMockMvc.perform(get("/api/ligne-devis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneDevis.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)));
    }

    @Test
    @Transactional
    public void getLigneDevis() throws Exception {
        // Initialize the database
        ligneDevisRepository.saveAndFlush(ligneDevis);

        // Get the ligneDevis
        restLigneDevisMockMvc.perform(get("/api/ligne-devis/{id}", ligneDevis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ligneDevis.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.prixUnitaire").value(DEFAULT_PRIX_UNITAIRE.doubleValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE));
    }

    @Test
    @Transactional
    public void getNonExistingLigneDevis() throws Exception {
        // Get the ligneDevis
        restLigneDevisMockMvc.perform(get("/api/ligne-devis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneDevis() throws Exception {
        // Initialize the database
        ligneDevisRepository.saveAndFlush(ligneDevis);
        ligneDevisSearchRepository.save(ligneDevis);
        int databaseSizeBeforeUpdate = ligneDevisRepository.findAll().size();

        // Update the ligneDevis
        LigneDevis updatedLigneDevis = ligneDevisRepository.findOne(ligneDevis.getId());
        // Disconnect from session so that the updates on updatedLigneDevis are not directly saved in db
        em.detach(updatedLigneDevis);
        updatedLigneDevis
            .designation(UPDATED_DESIGNATION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE);
        LigneDevisDTO ligneDevisDTO = ligneDevisMapper.toDto(updatedLigneDevis);

        restLigneDevisMockMvc.perform(put("/api/ligne-devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneDevisDTO)))
            .andExpect(status().isOk());

        // Validate the LigneDevis in the database
        List<LigneDevis> ligneDevisList = ligneDevisRepository.findAll();
        assertThat(ligneDevisList).hasSize(databaseSizeBeforeUpdate);
        LigneDevis testLigneDevis = ligneDevisList.get(ligneDevisList.size() - 1);
        assertThat(testLigneDevis.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testLigneDevis.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testLigneDevis.getQuantite()).isEqualTo(UPDATED_QUANTITE);

        // Validate the LigneDevis in Elasticsearch
        LigneDevis ligneDevisEs = ligneDevisSearchRepository.findOne(testLigneDevis.getId());
        assertThat(ligneDevisEs).isEqualToIgnoringGivenFields(testLigneDevis);
    }

    @Test
    @Transactional
    public void updateNonExistingLigneDevis() throws Exception {
        int databaseSizeBeforeUpdate = ligneDevisRepository.findAll().size();

        // Create the LigneDevis
        LigneDevisDTO ligneDevisDTO = ligneDevisMapper.toDto(ligneDevis);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLigneDevisMockMvc.perform(put("/api/ligne-devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneDevisDTO)))
            .andExpect(status().isCreated());

        // Validate the LigneDevis in the database
        List<LigneDevis> ligneDevisList = ligneDevisRepository.findAll();
        assertThat(ligneDevisList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLigneDevis() throws Exception {
        // Initialize the database
        ligneDevisRepository.saveAndFlush(ligneDevis);
        ligneDevisSearchRepository.save(ligneDevis);
        int databaseSizeBeforeDelete = ligneDevisRepository.findAll().size();

        // Get the ligneDevis
        restLigneDevisMockMvc.perform(delete("/api/ligne-devis/{id}", ligneDevis.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ligneDevisExistsInEs = ligneDevisSearchRepository.exists(ligneDevis.getId());
        assertThat(ligneDevisExistsInEs).isFalse();

        // Validate the database is empty
        List<LigneDevis> ligneDevisList = ligneDevisRepository.findAll();
        assertThat(ligneDevisList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLigneDevis() throws Exception {
        // Initialize the database
        ligneDevisRepository.saveAndFlush(ligneDevis);
        ligneDevisSearchRepository.save(ligneDevis);

        // Search the ligneDevis
        restLigneDevisMockMvc.perform(get("/api/_search/ligne-devis?query=id:" + ligneDevis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneDevis.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneDevis.class);
        LigneDevis ligneDevis1 = new LigneDevis();
        ligneDevis1.setId(1L);
        LigneDevis ligneDevis2 = new LigneDevis();
        ligneDevis2.setId(ligneDevis1.getId());
        assertThat(ligneDevis1).isEqualTo(ligneDevis2);
        ligneDevis2.setId(2L);
        assertThat(ligneDevis1).isNotEqualTo(ligneDevis2);
        ligneDevis1.setId(null);
        assertThat(ligneDevis1).isNotEqualTo(ligneDevis2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneDevisDTO.class);
        LigneDevisDTO ligneDevisDTO1 = new LigneDevisDTO();
        ligneDevisDTO1.setId(1L);
        LigneDevisDTO ligneDevisDTO2 = new LigneDevisDTO();
        assertThat(ligneDevisDTO1).isNotEqualTo(ligneDevisDTO2);
        ligneDevisDTO2.setId(ligneDevisDTO1.getId());
        assertThat(ligneDevisDTO1).isEqualTo(ligneDevisDTO2);
        ligneDevisDTO2.setId(2L);
        assertThat(ligneDevisDTO1).isNotEqualTo(ligneDevisDTO2);
        ligneDevisDTO1.setId(null);
        assertThat(ligneDevisDTO1).isNotEqualTo(ligneDevisDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ligneDevisMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ligneDevisMapper.fromId(null)).isNull();
    }
}
