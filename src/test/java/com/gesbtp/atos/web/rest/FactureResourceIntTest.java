package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.Facture;
import com.gesbtp.atos.repository.FactureRepository;
import com.gesbtp.atos.service.FactureService;
import com.gesbtp.atos.repository.search.FactureSearchRepository;
import com.gesbtp.atos.service.dto.FactureDTO;
import com.gesbtp.atos.service.mapper.FactureMapper;
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

/**
 * Test class for the FactureResource REST controller.
 *
 * @see FactureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class FactureResourceIntTest {

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_REMISE = 1D;
    private static final Double UPDATED_REMISE = 2D;

    private static final Boolean DEFAULT_VALIDER = false;
    private static final Boolean UPDATED_VALIDER = true;

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureMapper factureMapper;

    @Autowired
    private FactureService factureService;

    @Autowired
    private FactureSearchRepository factureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFactureMockMvc;

    private Facture facture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FactureResource factureResource = new FactureResource(factureService);
        this.restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource)
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
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
            .dateCreation(DEFAULT_DATE_CREATION)
            .remise(DEFAULT_REMISE)
            .valider(DEFAULT_VALIDER);
        return facture;
    }

    @Before
    public void initTest() {
        factureSearchRepository.deleteAll();
        facture = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testFacture.getRemise()).isEqualTo(DEFAULT_REMISE);
        assertThat(testFacture.isValider()).isEqualTo(DEFAULT_VALIDER);

        // Validate the Facture in Elasticsearch
        Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        assertThat(factureEs).isEqualToIgnoringGivenFields(testFacture);
    }

    @Test
    @Transactional
    public void createFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture with an existing ID
        facture.setId(1L);
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateCreation(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].remise").value(hasItem(DEFAULT_REMISE.doubleValue())))
            .andExpect(jsonPath("$.[*].valider").value(hasItem(DEFAULT_VALIDER.booleanValue())));
    }

    @Test
    @Transactional
    public void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.remise").value(DEFAULT_REMISE.doubleValue()))
            .andExpect(jsonPath("$.valider").value(DEFAULT_VALIDER.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        factureSearchRepository.save(facture);
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findOne(facture.getId());
        // Disconnect from session so that the updates on updatedFacture are not directly saved in db
        em.detach(updatedFacture);
        updatedFacture
            .dateCreation(UPDATED_DATE_CREATION)
            .remise(UPDATED_REMISE)
            .valider(UPDATED_VALIDER);
        FactureDTO factureDTO = factureMapper.toDto(updatedFacture);

        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testFacture.getRemise()).isEqualTo(UPDATED_REMISE);
        assertThat(testFacture.isValider()).isEqualTo(UPDATED_VALIDER);

        // Validate the Facture in Elasticsearch
        Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        assertThat(factureEs).isEqualToIgnoringGivenFields(testFacture);
    }

    @Test
    @Transactional
    public void updateNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        factureSearchRepository.save(facture);
        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Get the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean factureExistsInEs = factureSearchRepository.exists(facture.getId());
        assertThat(factureExistsInEs).isFalse();

        // Validate the database is empty
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        factureSearchRepository.save(facture);

        // Search the facture
        restFactureMockMvc.perform(get("/api/_search/factures?query=id:" + facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].remise").value(hasItem(DEFAULT_REMISE.doubleValue())))
            .andExpect(jsonPath("$.[*].valider").value(hasItem(DEFAULT_VALIDER.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture.class);
        Facture facture1 = new Facture();
        facture1.setId(1L);
        Facture facture2 = new Facture();
        facture2.setId(facture1.getId());
        assertThat(facture1).isEqualTo(facture2);
        facture2.setId(2L);
        assertThat(facture1).isNotEqualTo(facture2);
        facture1.setId(null);
        assertThat(facture1).isNotEqualTo(facture2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactureDTO.class);
        FactureDTO factureDTO1 = new FactureDTO();
        factureDTO1.setId(1L);
        FactureDTO factureDTO2 = new FactureDTO();
        assertThat(factureDTO1).isNotEqualTo(factureDTO2);
        factureDTO2.setId(factureDTO1.getId());
        assertThat(factureDTO1).isEqualTo(factureDTO2);
        factureDTO2.setId(2L);
        assertThat(factureDTO1).isNotEqualTo(factureDTO2);
        factureDTO1.setId(null);
        assertThat(factureDTO1).isNotEqualTo(factureDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(factureMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(factureMapper.fromId(null)).isNull();
    }
}
