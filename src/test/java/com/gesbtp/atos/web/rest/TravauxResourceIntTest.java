package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.Travaux;
import com.gesbtp.atos.repository.TravauxRepository;
import com.gesbtp.atos.service.TravauxService;
import com.gesbtp.atos.repository.search.TravauxSearchRepository;
import com.gesbtp.atos.service.dto.TravauxDTO;
import com.gesbtp.atos.service.mapper.TravauxMapper;
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
 * Test class for the TravauxResource REST controller.
 *
 * @see TravauxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class TravauxResourceIntTest {

    private static final String DEFAULT_NOM_TRAV = "AAAAAAAAAA";
    private static final String UPDATED_NOM_TRAV = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_TRAV = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_TRAV = "BBBBBBBBBB";

    @Autowired
    private TravauxRepository travauxRepository;

    @Autowired
    private TravauxMapper travauxMapper;

    @Autowired
    private TravauxService travauxService;

    @Autowired
    private TravauxSearchRepository travauxSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTravauxMockMvc;

    private Travaux travaux;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TravauxResource travauxResource = new TravauxResource(travauxService);
        this.restTravauxMockMvc = MockMvcBuilders.standaloneSetup(travauxResource)
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
    public static Travaux createEntity(EntityManager em) {
        Travaux travaux = new Travaux()
            .nomTrav(DEFAULT_NOM_TRAV)
            .descriptionTrav(DEFAULT_DESCRIPTION_TRAV);
        return travaux;
    }

    @Before
    public void initTest() {
        travauxSearchRepository.deleteAll();
        travaux = createEntity(em);
    }

    @Test
    @Transactional
    public void createTravaux() throws Exception {
        int databaseSizeBeforeCreate = travauxRepository.findAll().size();

        // Create the Travaux
        TravauxDTO travauxDTO = travauxMapper.toDto(travaux);
        restTravauxMockMvc.perform(post("/api/travauxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travauxDTO)))
            .andExpect(status().isCreated());

        // Validate the Travaux in the database
        List<Travaux> travauxList = travauxRepository.findAll();
        assertThat(travauxList).hasSize(databaseSizeBeforeCreate + 1);
        Travaux testTravaux = travauxList.get(travauxList.size() - 1);
        assertThat(testTravaux.getNomTrav()).isEqualTo(DEFAULT_NOM_TRAV);
        assertThat(testTravaux.getDescriptionTrav()).isEqualTo(DEFAULT_DESCRIPTION_TRAV);

        // Validate the Travaux in Elasticsearch
        Travaux travauxEs = travauxSearchRepository.findOne(testTravaux.getId());
        assertThat(travauxEs).isEqualToIgnoringGivenFields(testTravaux);
    }

    @Test
    @Transactional
    public void createTravauxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = travauxRepository.findAll().size();

        // Create the Travaux with an existing ID
        travaux.setId(1L);
        TravauxDTO travauxDTO = travauxMapper.toDto(travaux);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTravauxMockMvc.perform(post("/api/travauxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travauxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Travaux in the database
        List<Travaux> travauxList = travauxRepository.findAll();
        assertThat(travauxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomTravIsRequired() throws Exception {
        int databaseSizeBeforeTest = travauxRepository.findAll().size();
        // set the field null
        travaux.setNomTrav(null);

        // Create the Travaux, which fails.
        TravauxDTO travauxDTO = travauxMapper.toDto(travaux);

        restTravauxMockMvc.perform(post("/api/travauxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travauxDTO)))
            .andExpect(status().isBadRequest());

        List<Travaux> travauxList = travauxRepository.findAll();
        assertThat(travauxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTravauxes() throws Exception {
        // Initialize the database
        travauxRepository.saveAndFlush(travaux);

        // Get all the travauxList
        restTravauxMockMvc.perform(get("/api/travauxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(travaux.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomTrav").value(hasItem(DEFAULT_NOM_TRAV.toString())))
            .andExpect(jsonPath("$.[*].descriptionTrav").value(hasItem(DEFAULT_DESCRIPTION_TRAV.toString())));
    }

    @Test
    @Transactional
    public void getTravaux() throws Exception {
        // Initialize the database
        travauxRepository.saveAndFlush(travaux);

        // Get the travaux
        restTravauxMockMvc.perform(get("/api/travauxes/{id}", travaux.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(travaux.getId().intValue()))
            .andExpect(jsonPath("$.nomTrav").value(DEFAULT_NOM_TRAV.toString()))
            .andExpect(jsonPath("$.descriptionTrav").value(DEFAULT_DESCRIPTION_TRAV.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTravaux() throws Exception {
        // Get the travaux
        restTravauxMockMvc.perform(get("/api/travauxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTravaux() throws Exception {
        // Initialize the database
        travauxRepository.saveAndFlush(travaux);
        travauxSearchRepository.save(travaux);
        int databaseSizeBeforeUpdate = travauxRepository.findAll().size();

        // Update the travaux
        Travaux updatedTravaux = travauxRepository.findOne(travaux.getId());
        // Disconnect from session so that the updates on updatedTravaux are not directly saved in db
        em.detach(updatedTravaux);
        updatedTravaux
            .nomTrav(UPDATED_NOM_TRAV)
            .descriptionTrav(UPDATED_DESCRIPTION_TRAV);
        TravauxDTO travauxDTO = travauxMapper.toDto(updatedTravaux);

        restTravauxMockMvc.perform(put("/api/travauxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travauxDTO)))
            .andExpect(status().isOk());

        // Validate the Travaux in the database
        List<Travaux> travauxList = travauxRepository.findAll();
        assertThat(travauxList).hasSize(databaseSizeBeforeUpdate);
        Travaux testTravaux = travauxList.get(travauxList.size() - 1);
        assertThat(testTravaux.getNomTrav()).isEqualTo(UPDATED_NOM_TRAV);
        assertThat(testTravaux.getDescriptionTrav()).isEqualTo(UPDATED_DESCRIPTION_TRAV);

        // Validate the Travaux in Elasticsearch
        Travaux travauxEs = travauxSearchRepository.findOne(testTravaux.getId());
        assertThat(travauxEs).isEqualToIgnoringGivenFields(testTravaux);
    }

    @Test
    @Transactional
    public void updateNonExistingTravaux() throws Exception {
        int databaseSizeBeforeUpdate = travauxRepository.findAll().size();

        // Create the Travaux
        TravauxDTO travauxDTO = travauxMapper.toDto(travaux);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTravauxMockMvc.perform(put("/api/travauxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travauxDTO)))
            .andExpect(status().isCreated());

        // Validate the Travaux in the database
        List<Travaux> travauxList = travauxRepository.findAll();
        assertThat(travauxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTravaux() throws Exception {
        // Initialize the database
        travauxRepository.saveAndFlush(travaux);
        travauxSearchRepository.save(travaux);
        int databaseSizeBeforeDelete = travauxRepository.findAll().size();

        // Get the travaux
        restTravauxMockMvc.perform(delete("/api/travauxes/{id}", travaux.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean travauxExistsInEs = travauxSearchRepository.exists(travaux.getId());
        assertThat(travauxExistsInEs).isFalse();

        // Validate the database is empty
        List<Travaux> travauxList = travauxRepository.findAll();
        assertThat(travauxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTravaux() throws Exception {
        // Initialize the database
        travauxRepository.saveAndFlush(travaux);
        travauxSearchRepository.save(travaux);

        // Search the travaux
        restTravauxMockMvc.perform(get("/api/_search/travauxes?query=id:" + travaux.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(travaux.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomTrav").value(hasItem(DEFAULT_NOM_TRAV.toString())))
            .andExpect(jsonPath("$.[*].descriptionTrav").value(hasItem(DEFAULT_DESCRIPTION_TRAV.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Travaux.class);
        Travaux travaux1 = new Travaux();
        travaux1.setId(1L);
        Travaux travaux2 = new Travaux();
        travaux2.setId(travaux1.getId());
        assertThat(travaux1).isEqualTo(travaux2);
        travaux2.setId(2L);
        assertThat(travaux1).isNotEqualTo(travaux2);
        travaux1.setId(null);
        assertThat(travaux1).isNotEqualTo(travaux2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TravauxDTO.class);
        TravauxDTO travauxDTO1 = new TravauxDTO();
        travauxDTO1.setId(1L);
        TravauxDTO travauxDTO2 = new TravauxDTO();
        assertThat(travauxDTO1).isNotEqualTo(travauxDTO2);
        travauxDTO2.setId(travauxDTO1.getId());
        assertThat(travauxDTO1).isEqualTo(travauxDTO2);
        travauxDTO2.setId(2L);
        assertThat(travauxDTO1).isNotEqualTo(travauxDTO2);
        travauxDTO1.setId(null);
        assertThat(travauxDTO1).isNotEqualTo(travauxDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(travauxMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(travauxMapper.fromId(null)).isNull();
    }
}
