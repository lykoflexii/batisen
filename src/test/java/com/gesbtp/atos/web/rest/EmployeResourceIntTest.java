package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.Employe;
import com.gesbtp.atos.repository.EmployeRepository;
import com.gesbtp.atos.service.EmployeService;
import com.gesbtp.atos.repository.search.EmployeSearchRepository;
import com.gesbtp.atos.service.dto.EmployeDTO;
import com.gesbtp.atos.service.mapper.EmployeMapper;
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

import com.gesbtp.atos.domain.enumeration.Sexe;
/**
 * Test class for the EmployeResource REST controller.
 *
 * @see EmployeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class EmployeResourceIntTest {

    private static final String DEFAULT_NOM_EMPLOYE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_EMPLOYE = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_EMPLOYE = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_EMPLOYE = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TELEPHONE_EMPLOYE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE_EMPLOYE = "BBBBBBBBBB";

    private static final Double DEFAULT_SALAIRE = 1D;
    private static final Double UPDATED_SALAIRE = 2D;

    private static final Sexe DEFAULT_SEXE = Sexe.MASCULIN;
    private static final Sexe UPDATED_SEXE = Sexe.FEMININ;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EmployeMapper employeMapper;

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeSearchRepository employeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeMockMvc;

    private Employe employe;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeResource employeResource = new EmployeResource(employeService);
        this.restEmployeMockMvc = MockMvcBuilders.standaloneSetup(employeResource)
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
    public static Employe createEntity(EntityManager em) {
        Employe employe = new Employe()
            .nomEmploye(DEFAULT_NOM_EMPLOYE)
            .prenomEmploye(DEFAULT_PRENOM_EMPLOYE)
            .matricule(DEFAULT_MATRICULE)
            .fonction(DEFAULT_FONCTION)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .telephoneEmploye(DEFAULT_TELEPHONE_EMPLOYE)
            .salaire(DEFAULT_SALAIRE)
            .sexe(DEFAULT_SEXE);
        return employe;
    }

    @Before
    public void initTest() {
        employeSearchRepository.deleteAll();
        employe = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmploye() throws Exception {
        int databaseSizeBeforeCreate = employeRepository.findAll().size();

        // Create the Employe
        EmployeDTO employeDTO = employeMapper.toDto(employe);
        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeCreate + 1);
        Employe testEmploye = employeList.get(employeList.size() - 1);
        assertThat(testEmploye.getNomEmploye()).isEqualTo(DEFAULT_NOM_EMPLOYE);
        assertThat(testEmploye.getPrenomEmploye()).isEqualTo(DEFAULT_PRENOM_EMPLOYE);
        assertThat(testEmploye.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testEmploye.getFonction()).isEqualTo(DEFAULT_FONCTION);
        assertThat(testEmploye.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testEmploye.getTelephoneEmploye()).isEqualTo(DEFAULT_TELEPHONE_EMPLOYE);
        assertThat(testEmploye.getSalaire()).isEqualTo(DEFAULT_SALAIRE);
        assertThat(testEmploye.getSexe()).isEqualTo(DEFAULT_SEXE);

        // Validate the Employe in Elasticsearch
        Employe employeEs = employeSearchRepository.findOne(testEmploye.getId());
        assertThat(employeEs).isEqualToIgnoringGivenFields(testEmploye);
    }

    @Test
    @Transactional
    public void createEmployeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeRepository.findAll().size();

        // Create the Employe with an existing ID
        employe.setId(1L);
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomEmployeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setNomEmploye(null);

        // Create the Employe, which fails.
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeDTO)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomEmployeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setPrenomEmploye(null);

        // Create the Employe, which fails.
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeDTO)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMatriculeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setMatricule(null);

        // Create the Employe, which fails.
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeDTO)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFonctionIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setFonction(null);

        // Create the Employe, which fails.
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeDTO)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployes() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        // Get all the employeList
        restEmployeMockMvc.perform(get("/api/employes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEmploye").value(hasItem(DEFAULT_NOM_EMPLOYE.toString())))
            .andExpect(jsonPath("$.[*].prenomEmploye").value(hasItem(DEFAULT_PRENOM_EMPLOYE.toString())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].telephoneEmploye").value(hasItem(DEFAULT_TELEPHONE_EMPLOYE.toString())))
            .andExpect(jsonPath("$.[*].salaire").value(hasItem(DEFAULT_SALAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));
    }

    @Test
    @Transactional
    public void getEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        // Get the employe
        restEmployeMockMvc.perform(get("/api/employes/{id}", employe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employe.getId().intValue()))
            .andExpect(jsonPath("$.nomEmploye").value(DEFAULT_NOM_EMPLOYE.toString()))
            .andExpect(jsonPath("$.prenomEmploye").value(DEFAULT_PRENOM_EMPLOYE.toString()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE.toString()))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION.toString()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.telephoneEmploye").value(DEFAULT_TELEPHONE_EMPLOYE.toString()))
            .andExpect(jsonPath("$.salaire").value(DEFAULT_SALAIRE.doubleValue()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmploye() throws Exception {
        // Get the employe
        restEmployeMockMvc.perform(get("/api/employes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);
        employeSearchRepository.save(employe);
        int databaseSizeBeforeUpdate = employeRepository.findAll().size();

        // Update the employe
        Employe updatedEmploye = employeRepository.findOne(employe.getId());
        // Disconnect from session so that the updates on updatedEmploye are not directly saved in db
        em.detach(updatedEmploye);
        updatedEmploye
            .nomEmploye(UPDATED_NOM_EMPLOYE)
            .prenomEmploye(UPDATED_PRENOM_EMPLOYE)
            .matricule(UPDATED_MATRICULE)
            .fonction(UPDATED_FONCTION)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .telephoneEmploye(UPDATED_TELEPHONE_EMPLOYE)
            .salaire(UPDATED_SALAIRE)
            .sexe(UPDATED_SEXE);
        EmployeDTO employeDTO = employeMapper.toDto(updatedEmploye);

        restEmployeMockMvc.perform(put("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeDTO)))
            .andExpect(status().isOk());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeUpdate);
        Employe testEmploye = employeList.get(employeList.size() - 1);
        assertThat(testEmploye.getNomEmploye()).isEqualTo(UPDATED_NOM_EMPLOYE);
        assertThat(testEmploye.getPrenomEmploye()).isEqualTo(UPDATED_PRENOM_EMPLOYE);
        assertThat(testEmploye.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testEmploye.getFonction()).isEqualTo(UPDATED_FONCTION);
        assertThat(testEmploye.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testEmploye.getTelephoneEmploye()).isEqualTo(UPDATED_TELEPHONE_EMPLOYE);
        assertThat(testEmploye.getSalaire()).isEqualTo(UPDATED_SALAIRE);
        assertThat(testEmploye.getSexe()).isEqualTo(UPDATED_SEXE);

        // Validate the Employe in Elasticsearch
        Employe employeEs = employeSearchRepository.findOne(testEmploye.getId());
        assertThat(employeEs).isEqualToIgnoringGivenFields(testEmploye);
    }

    @Test
    @Transactional
    public void updateNonExistingEmploye() throws Exception {
        int databaseSizeBeforeUpdate = employeRepository.findAll().size();

        // Create the Employe
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmployeMockMvc.perform(put("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);
        employeSearchRepository.save(employe);
        int databaseSizeBeforeDelete = employeRepository.findAll().size();

        // Get the employe
        restEmployeMockMvc.perform(delete("/api/employes/{id}", employe.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean employeExistsInEs = employeSearchRepository.exists(employe.getId());
        assertThat(employeExistsInEs).isFalse();

        // Validate the database is empty
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);
        employeSearchRepository.save(employe);

        // Search the employe
        restEmployeMockMvc.perform(get("/api/_search/employes?query=id:" + employe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEmploye").value(hasItem(DEFAULT_NOM_EMPLOYE.toString())))
            .andExpect(jsonPath("$.[*].prenomEmploye").value(hasItem(DEFAULT_PRENOM_EMPLOYE.toString())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].telephoneEmploye").value(hasItem(DEFAULT_TELEPHONE_EMPLOYE.toString())))
            .andExpect(jsonPath("$.[*].salaire").value(hasItem(DEFAULT_SALAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employe.class);
        Employe employe1 = new Employe();
        employe1.setId(1L);
        Employe employe2 = new Employe();
        employe2.setId(employe1.getId());
        assertThat(employe1).isEqualTo(employe2);
        employe2.setId(2L);
        assertThat(employe1).isNotEqualTo(employe2);
        employe1.setId(null);
        assertThat(employe1).isNotEqualTo(employe2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeDTO.class);
        EmployeDTO employeDTO1 = new EmployeDTO();
        employeDTO1.setId(1L);
        EmployeDTO employeDTO2 = new EmployeDTO();
        assertThat(employeDTO1).isNotEqualTo(employeDTO2);
        employeDTO2.setId(employeDTO1.getId());
        assertThat(employeDTO1).isEqualTo(employeDTO2);
        employeDTO2.setId(2L);
        assertThat(employeDTO1).isNotEqualTo(employeDTO2);
        employeDTO1.setId(null);
        assertThat(employeDTO1).isNotEqualTo(employeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(employeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(employeMapper.fromId(null)).isNull();
    }
}
