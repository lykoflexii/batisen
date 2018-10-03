package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.Devis;
import com.gesbtp.atos.repository.DevisRepository;
import com.gesbtp.atos.service.DevisService;
import com.gesbtp.atos.repository.search.DevisSearchRepository;
import com.gesbtp.atos.service.dto.DevisDTO;
import com.gesbtp.atos.service.mapper.DevisMapper;
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
 * Test class for the DevisResource REST controller.
 *
 * @see DevisResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class DevisResourceIntTest {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_VALIDITE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALIDITE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_TVA = 1F;
    private static final Float UPDATED_TVA = 2F;

    private static final Double DEFAULT_COUT_MAIN_DOEUVRE = 1D;
    private static final Double UPDATED_COUT_MAIN_DOEUVRE = 2D;

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private DevisMapper devisMapper;

    @Autowired
    private DevisService devisService;

    @Autowired
    private DevisSearchRepository devisSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDevisMockMvc;

    private Devis devis;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DevisResource devisResource = new DevisResource(devisService);
        this.restDevisMockMvc = MockMvcBuilders.standaloneSetup(devisResource)
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
    public static Devis createEntity(EntityManager em) {
        Devis devis = new Devis()
            .titre(DEFAULT_TITRE)
            .dateDeCreation(DEFAULT_DATE_DE_CREATION)
            .validite(DEFAULT_VALIDITE)
            .tva(DEFAULT_TVA)
            .coutMainDoeuvre(DEFAULT_COUT_MAIN_DOEUVRE);
        return devis;
    }

    @Before
    public void initTest() {
        devisSearchRepository.deleteAll();
        devis = createEntity(em);
    }

    @Test
    @Transactional
    public void createDevis() throws Exception {
        int databaseSizeBeforeCreate = devisRepository.findAll().size();

        // Create the Devis
        DevisDTO devisDTO = devisMapper.toDto(devis);
        restDevisMockMvc.perform(post("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devisDTO)))
            .andExpect(status().isCreated());

        // Validate the Devis in the database
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeCreate + 1);
        Devis testDevis = devisList.get(devisList.size() - 1);
        assertThat(testDevis.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testDevis.getDateDeCreation()).isEqualTo(DEFAULT_DATE_DE_CREATION);
        assertThat(testDevis.getValidite()).isEqualTo(DEFAULT_VALIDITE);
        assertThat(testDevis.getTva()).isEqualTo(DEFAULT_TVA);
        assertThat(testDevis.getCoutMainDoeuvre()).isEqualTo(DEFAULT_COUT_MAIN_DOEUVRE);

        // Validate the Devis in Elasticsearch
        Devis devisEs = devisSearchRepository.findOne(testDevis.getId());
        assertThat(devisEs).isEqualToIgnoringGivenFields(testDevis);
    }

    @Test
    @Transactional
    public void createDevisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = devisRepository.findAll().size();

        // Create the Devis with an existing ID
        devis.setId(1L);
        DevisDTO devisDTO = devisMapper.toDto(devis);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDevisMockMvc.perform(post("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Devis in the database
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateDeCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = devisRepository.findAll().size();
        // set the field null
        devis.setDateDeCreation(null);

        // Create the Devis, which fails.
        DevisDTO devisDTO = devisMapper.toDto(devis);

        restDevisMockMvc.perform(post("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devisDTO)))
            .andExpect(status().isBadRequest());

        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValiditeIsRequired() throws Exception {
        int databaseSizeBeforeTest = devisRepository.findAll().size();
        // set the field null
        devis.setValidite(null);

        // Create the Devis, which fails.
        DevisDTO devisDTO = devisMapper.toDto(devis);

        restDevisMockMvc.perform(post("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devisDTO)))
            .andExpect(status().isBadRequest());

        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);

        // Get all the devisList
        restDevisMockMvc.perform(get("/api/devis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devis.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].dateDeCreation").value(hasItem(DEFAULT_DATE_DE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].validite").value(hasItem(DEFAULT_VALIDITE.toString())))
            .andExpect(jsonPath("$.[*].tva").value(hasItem(DEFAULT_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].coutMainDoeuvre").value(hasItem(DEFAULT_COUT_MAIN_DOEUVRE.doubleValue())));
    }

    @Test
    @Transactional
    public void getDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);

        // Get the devis
        restDevisMockMvc.perform(get("/api/devis/{id}", devis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(devis.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.dateDeCreation").value(DEFAULT_DATE_DE_CREATION.toString()))
            .andExpect(jsonPath("$.validite").value(DEFAULT_VALIDITE.toString()))
            .andExpect(jsonPath("$.tva").value(DEFAULT_TVA.doubleValue()))
            .andExpect(jsonPath("$.coutMainDoeuvre").value(DEFAULT_COUT_MAIN_DOEUVRE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDevis() throws Exception {
        // Get the devis
        restDevisMockMvc.perform(get("/api/devis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);
        devisSearchRepository.save(devis);
        int databaseSizeBeforeUpdate = devisRepository.findAll().size();

        // Update the devis
        Devis updatedDevis = devisRepository.findOne(devis.getId());
        // Disconnect from session so that the updates on updatedDevis are not directly saved in db
        em.detach(updatedDevis);
        updatedDevis
            .titre(UPDATED_TITRE)
            .dateDeCreation(UPDATED_DATE_DE_CREATION)
            .validite(UPDATED_VALIDITE)
            .tva(UPDATED_TVA)
            .coutMainDoeuvre(UPDATED_COUT_MAIN_DOEUVRE);
        DevisDTO devisDTO = devisMapper.toDto(updatedDevis);

        restDevisMockMvc.perform(put("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devisDTO)))
            .andExpect(status().isOk());

        // Validate the Devis in the database
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeUpdate);
        Devis testDevis = devisList.get(devisList.size() - 1);
        assertThat(testDevis.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testDevis.getDateDeCreation()).isEqualTo(UPDATED_DATE_DE_CREATION);
        assertThat(testDevis.getValidite()).isEqualTo(UPDATED_VALIDITE);
        assertThat(testDevis.getTva()).isEqualTo(UPDATED_TVA);
        assertThat(testDevis.getCoutMainDoeuvre()).isEqualTo(UPDATED_COUT_MAIN_DOEUVRE);

        // Validate the Devis in Elasticsearch
        Devis devisEs = devisSearchRepository.findOne(testDevis.getId());
        assertThat(devisEs).isEqualToIgnoringGivenFields(testDevis);
    }

    @Test
    @Transactional
    public void updateNonExistingDevis() throws Exception {
        int databaseSizeBeforeUpdate = devisRepository.findAll().size();

        // Create the Devis
        DevisDTO devisDTO = devisMapper.toDto(devis);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDevisMockMvc.perform(put("/api/devis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(devisDTO)))
            .andExpect(status().isCreated());

        // Validate the Devis in the database
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);
        devisSearchRepository.save(devis);
        int databaseSizeBeforeDelete = devisRepository.findAll().size();

        // Get the devis
        restDevisMockMvc.perform(delete("/api/devis/{id}", devis.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean devisExistsInEs = devisSearchRepository.exists(devis.getId());
        assertThat(devisExistsInEs).isFalse();

        // Validate the database is empty
        List<Devis> devisList = devisRepository.findAll();
        assertThat(devisList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDevis() throws Exception {
        // Initialize the database
        devisRepository.saveAndFlush(devis);
        devisSearchRepository.save(devis);

        // Search the devis
        restDevisMockMvc.perform(get("/api/_search/devis?query=id:" + devis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devis.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].dateDeCreation").value(hasItem(DEFAULT_DATE_DE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].validite").value(hasItem(DEFAULT_VALIDITE.toString())))
            .andExpect(jsonPath("$.[*].tva").value(hasItem(DEFAULT_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].coutMainDoeuvre").value(hasItem(DEFAULT_COUT_MAIN_DOEUVRE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Devis.class);
        Devis devis1 = new Devis();
        devis1.setId(1L);
        Devis devis2 = new Devis();
        devis2.setId(devis1.getId());
        assertThat(devis1).isEqualTo(devis2);
        devis2.setId(2L);
        assertThat(devis1).isNotEqualTo(devis2);
        devis1.setId(null);
        assertThat(devis1).isNotEqualTo(devis2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DevisDTO.class);
        DevisDTO devisDTO1 = new DevisDTO();
        devisDTO1.setId(1L);
        DevisDTO devisDTO2 = new DevisDTO();
        assertThat(devisDTO1).isNotEqualTo(devisDTO2);
        devisDTO2.setId(devisDTO1.getId());
        assertThat(devisDTO1).isEqualTo(devisDTO2);
        devisDTO2.setId(2L);
        assertThat(devisDTO1).isNotEqualTo(devisDTO2);
        devisDTO1.setId(null);
        assertThat(devisDTO1).isNotEqualTo(devisDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(devisMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(devisMapper.fromId(null)).isNull();
    }
}
