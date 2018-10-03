package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.LigneFacture;
import com.gesbtp.atos.repository.LigneFactureRepository;
import com.gesbtp.atos.service.LigneFactureService;
import com.gesbtp.atos.repository.search.LigneFactureSearchRepository;
import com.gesbtp.atos.service.dto.LigneFactureDTO;
import com.gesbtp.atos.service.mapper.LigneFactureMapper;
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
 * Test class for the LigneFactureResource REST controller.
 *
 * @see LigneFactureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class LigneFactureResourceIntTest {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX_UNITAIRE = 1D;
    private static final Double UPDATED_PRIX_UNITAIRE = 2D;

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Integer DEFAULT_QUANTITE_RETENUE = 1;
    private static final Integer UPDATED_QUANTITE_RETENUE = 2;

    @Autowired
    private LigneFactureRepository ligneFactureRepository;

    @Autowired
    private LigneFactureMapper ligneFactureMapper;

    @Autowired
    private LigneFactureService ligneFactureService;

    @Autowired
    private LigneFactureSearchRepository ligneFactureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLigneFactureMockMvc;

    private LigneFacture ligneFacture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LigneFactureResource ligneFactureResource = new LigneFactureResource(ligneFactureService);
        this.restLigneFactureMockMvc = MockMvcBuilders.standaloneSetup(ligneFactureResource)
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
    public static LigneFacture createEntity(EntityManager em) {
        LigneFacture ligneFacture = new LigneFacture()
            .designation(DEFAULT_DESIGNATION)
            .prixUnitaire(DEFAULT_PRIX_UNITAIRE)
            .quantite(DEFAULT_QUANTITE)
            .quantiteRetenue(DEFAULT_QUANTITE_RETENUE);
        return ligneFacture;
    }

    @Before
    public void initTest() {
        ligneFactureSearchRepository.deleteAll();
        ligneFacture = createEntity(em);
    }

    @Test
    @Transactional
    public void createLigneFacture() throws Exception {
        int databaseSizeBeforeCreate = ligneFactureRepository.findAll().size();

        // Create the LigneFacture
        LigneFactureDTO ligneFactureDTO = ligneFactureMapper.toDto(ligneFacture);
        restLigneFactureMockMvc.perform(post("/api/ligne-factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneFactureDTO)))
            .andExpect(status().isCreated());

        // Validate the LigneFacture in the database
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeCreate + 1);
        LigneFacture testLigneFacture = ligneFactureList.get(ligneFactureList.size() - 1);
        assertThat(testLigneFacture.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testLigneFacture.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testLigneFacture.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneFacture.getQuantiteRetenue()).isEqualTo(DEFAULT_QUANTITE_RETENUE);

        // Validate the LigneFacture in Elasticsearch
        LigneFacture ligneFactureEs = ligneFactureSearchRepository.findOne(testLigneFacture.getId());
        assertThat(ligneFactureEs).isEqualToIgnoringGivenFields(testLigneFacture);
    }

    @Test
    @Transactional
    public void createLigneFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligneFactureRepository.findAll().size();

        // Create the LigneFacture with an existing ID
        ligneFacture.setId(1L);
        LigneFactureDTO ligneFactureDTO = ligneFactureMapper.toDto(ligneFacture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneFactureMockMvc.perform(post("/api/ligne-factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneFactureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LigneFacture in the database
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneFactureRepository.findAll().size();
        // set the field null
        ligneFacture.setDesignation(null);

        // Create the LigneFacture, which fails.
        LigneFactureDTO ligneFactureDTO = ligneFactureMapper.toDto(ligneFacture);

        restLigneFactureMockMvc.perform(post("/api/ligne-factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneFactureDTO)))
            .andExpect(status().isBadRequest());

        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixUnitaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneFactureRepository.findAll().size();
        // set the field null
        ligneFacture.setPrixUnitaire(null);

        // Create the LigneFacture, which fails.
        LigneFactureDTO ligneFactureDTO = ligneFactureMapper.toDto(ligneFacture);

        restLigneFactureMockMvc.perform(post("/api/ligne-factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneFactureDTO)))
            .andExpect(status().isBadRequest());

        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneFactureRepository.findAll().size();
        // set the field null
        ligneFacture.setQuantite(null);

        // Create the LigneFacture, which fails.
        LigneFactureDTO ligneFactureDTO = ligneFactureMapper.toDto(ligneFacture);

        restLigneFactureMockMvc.perform(post("/api/ligne-factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneFactureDTO)))
            .andExpect(status().isBadRequest());

        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLigneFactures() throws Exception {
        // Initialize the database
        ligneFactureRepository.saveAndFlush(ligneFacture);

        // Get all the ligneFactureList
        restLigneFactureMockMvc.perform(get("/api/ligne-factures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneFacture.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].quantiteRetenue").value(hasItem(DEFAULT_QUANTITE_RETENUE)));
    }

    @Test
    @Transactional
    public void getLigneFacture() throws Exception {
        // Initialize the database
        ligneFactureRepository.saveAndFlush(ligneFacture);

        // Get the ligneFacture
        restLigneFactureMockMvc.perform(get("/api/ligne-factures/{id}", ligneFacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ligneFacture.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.prixUnitaire").value(DEFAULT_PRIX_UNITAIRE.doubleValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.quantiteRetenue").value(DEFAULT_QUANTITE_RETENUE));
    }

    @Test
    @Transactional
    public void getNonExistingLigneFacture() throws Exception {
        // Get the ligneFacture
        restLigneFactureMockMvc.perform(get("/api/ligne-factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneFacture() throws Exception {
        // Initialize the database
        ligneFactureRepository.saveAndFlush(ligneFacture);
        ligneFactureSearchRepository.save(ligneFacture);
        int databaseSizeBeforeUpdate = ligneFactureRepository.findAll().size();

        // Update the ligneFacture
        LigneFacture updatedLigneFacture = ligneFactureRepository.findOne(ligneFacture.getId());
        // Disconnect from session so that the updates on updatedLigneFacture are not directly saved in db
        em.detach(updatedLigneFacture);
        updatedLigneFacture
            .designation(UPDATED_DESIGNATION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .quantiteRetenue(UPDATED_QUANTITE_RETENUE);
        LigneFactureDTO ligneFactureDTO = ligneFactureMapper.toDto(updatedLigneFacture);

        restLigneFactureMockMvc.perform(put("/api/ligne-factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneFactureDTO)))
            .andExpect(status().isOk());

        // Validate the LigneFacture in the database
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeUpdate);
        LigneFacture testLigneFacture = ligneFactureList.get(ligneFactureList.size() - 1);
        assertThat(testLigneFacture.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testLigneFacture.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testLigneFacture.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneFacture.getQuantiteRetenue()).isEqualTo(UPDATED_QUANTITE_RETENUE);

        // Validate the LigneFacture in Elasticsearch
        LigneFacture ligneFactureEs = ligneFactureSearchRepository.findOne(testLigneFacture.getId());
        assertThat(ligneFactureEs).isEqualToIgnoringGivenFields(testLigneFacture);
    }

    @Test
    @Transactional
    public void updateNonExistingLigneFacture() throws Exception {
        int databaseSizeBeforeUpdate = ligneFactureRepository.findAll().size();

        // Create the LigneFacture
        LigneFactureDTO ligneFactureDTO = ligneFactureMapper.toDto(ligneFacture);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLigneFactureMockMvc.perform(put("/api/ligne-factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneFactureDTO)))
            .andExpect(status().isCreated());

        // Validate the LigneFacture in the database
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLigneFacture() throws Exception {
        // Initialize the database
        ligneFactureRepository.saveAndFlush(ligneFacture);
        ligneFactureSearchRepository.save(ligneFacture);
        int databaseSizeBeforeDelete = ligneFactureRepository.findAll().size();

        // Get the ligneFacture
        restLigneFactureMockMvc.perform(delete("/api/ligne-factures/{id}", ligneFacture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ligneFactureExistsInEs = ligneFactureSearchRepository.exists(ligneFacture.getId());
        assertThat(ligneFactureExistsInEs).isFalse();

        // Validate the database is empty
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLigneFacture() throws Exception {
        // Initialize the database
        ligneFactureRepository.saveAndFlush(ligneFacture);
        ligneFactureSearchRepository.save(ligneFacture);

        // Search the ligneFacture
        restLigneFactureMockMvc.perform(get("/api/_search/ligne-factures?query=id:" + ligneFacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneFacture.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].quantiteRetenue").value(hasItem(DEFAULT_QUANTITE_RETENUE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneFacture.class);
        LigneFacture ligneFacture1 = new LigneFacture();
        ligneFacture1.setId(1L);
        LigneFacture ligneFacture2 = new LigneFacture();
        ligneFacture2.setId(ligneFacture1.getId());
        assertThat(ligneFacture1).isEqualTo(ligneFacture2);
        ligneFacture2.setId(2L);
        assertThat(ligneFacture1).isNotEqualTo(ligneFacture2);
        ligneFacture1.setId(null);
        assertThat(ligneFacture1).isNotEqualTo(ligneFacture2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneFactureDTO.class);
        LigneFactureDTO ligneFactureDTO1 = new LigneFactureDTO();
        ligneFactureDTO1.setId(1L);
        LigneFactureDTO ligneFactureDTO2 = new LigneFactureDTO();
        assertThat(ligneFactureDTO1).isNotEqualTo(ligneFactureDTO2);
        ligneFactureDTO2.setId(ligneFactureDTO1.getId());
        assertThat(ligneFactureDTO1).isEqualTo(ligneFactureDTO2);
        ligneFactureDTO2.setId(2L);
        assertThat(ligneFactureDTO1).isNotEqualTo(ligneFactureDTO2);
        ligneFactureDTO1.setId(null);
        assertThat(ligneFactureDTO1).isNotEqualTo(ligneFactureDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ligneFactureMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ligneFactureMapper.fromId(null)).isNull();
    }
}
