package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.Client;
import com.gesbtp.atos.repository.ClientRepository;
import com.gesbtp.atos.service.ClientService;
import com.gesbtp.atos.repository.search.ClientSearchRepository;
import com.gesbtp.atos.service.dto.ClientDTO;
import com.gesbtp.atos.service.mapper.ClientMapper;
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

import com.gesbtp.atos.domain.enumeration.Type;
/**
 * Test class for the ClientResource REST controller.
 *
 * @see ClientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class ClientResourceIntTest {

    private static final String DEFAULT_NOM_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_VILLE_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_CLIENT = "BBBBBBBBBB";

    private static final Type DEFAULT_TYPE_CLIENT = Type.PARTICULIER;
    private static final Type UPDATED_TYPE_CLIENT = Type.PROFESSIONNEL;

    private static final String DEFAULT_PRENOM_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_COMMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMMERCIAL = "BBBBBBBBBB";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientSearchRepository clientSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClientMockMvc;

    private Client client;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClientResource clientResource = new ClientResource(clientService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
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
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .nomClient(DEFAULT_NOM_CLIENT)
            .villeClient(DEFAULT_VILLE_CLIENT)
            .adresseClient(DEFAULT_ADRESSE_CLIENT)
            .telephoneClient(DEFAULT_TELEPHONE_CLIENT)
            .fax(DEFAULT_FAX)
            .emailClient(DEFAULT_EMAIL_CLIENT)
            .typeClient(DEFAULT_TYPE_CLIENT)
            .prenomClient(DEFAULT_PRENOM_CLIENT)
            .nomCommercial(DEFAULT_NOM_COMMERCIAL);
        return client;
    }

    @Before
    public void initTest() {
        clientSearchRepository.deleteAll();
        client = createEntity(em);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNomClient()).isEqualTo(DEFAULT_NOM_CLIENT);
        assertThat(testClient.getVilleClient()).isEqualTo(DEFAULT_VILLE_CLIENT);
        assertThat(testClient.getAdresseClient()).isEqualTo(DEFAULT_ADRESSE_CLIENT);
        assertThat(testClient.getTelephoneClient()).isEqualTo(DEFAULT_TELEPHONE_CLIENT);
        assertThat(testClient.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testClient.getEmailClient()).isEqualTo(DEFAULT_EMAIL_CLIENT);
        assertThat(testClient.getTypeClient()).isEqualTo(DEFAULT_TYPE_CLIENT);
        assertThat(testClient.getPrenomClient()).isEqualTo(DEFAULT_PRENOM_CLIENT);
        assertThat(testClient.getNomCommercial()).isEqualTo(DEFAULT_NOM_COMMERCIAL);

        // Validate the Client in Elasticsearch
        Client clientEs = clientSearchRepository.findOne(testClient.getId());
        assertThat(clientEs).isEqualToIgnoringGivenFields(testClient);
    }

    @Test
    @Transactional
    public void createClientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setNomClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVilleClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setVilleClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setAdresseClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setEmailClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setTypeClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomClient").value(hasItem(DEFAULT_NOM_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].villeClient").value(hasItem(DEFAULT_VILLE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].adresseClient").value(hasItem(DEFAULT_ADRESSE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].telephoneClient").value(hasItem(DEFAULT_TELEPHONE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX.toString())))
            .andExpect(jsonPath("$.[*].emailClient").value(hasItem(DEFAULT_EMAIL_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].typeClient").value(hasItem(DEFAULT_TYPE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].prenomClient").value(hasItem(DEFAULT_PRENOM_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].nomCommercial").value(hasItem(DEFAULT_NOM_COMMERCIAL.toString())));
    }

    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.nomClient").value(DEFAULT_NOM_CLIENT.toString()))
            .andExpect(jsonPath("$.villeClient").value(DEFAULT_VILLE_CLIENT.toString()))
            .andExpect(jsonPath("$.adresseClient").value(DEFAULT_ADRESSE_CLIENT.toString()))
            .andExpect(jsonPath("$.telephoneClient").value(DEFAULT_TELEPHONE_CLIENT.toString()))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX.toString()))
            .andExpect(jsonPath("$.emailClient").value(DEFAULT_EMAIL_CLIENT.toString()))
            .andExpect(jsonPath("$.typeClient").value(DEFAULT_TYPE_CLIENT.toString()))
            .andExpect(jsonPath("$.prenomClient").value(DEFAULT_PRENOM_CLIENT.toString()))
            .andExpect(jsonPath("$.nomCommercial").value(DEFAULT_NOM_COMMERCIAL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        clientSearchRepository.save(client);
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findOne(client.getId());
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .nomClient(UPDATED_NOM_CLIENT)
            .villeClient(UPDATED_VILLE_CLIENT)
            .adresseClient(UPDATED_ADRESSE_CLIENT)
            .telephoneClient(UPDATED_TELEPHONE_CLIENT)
            .fax(UPDATED_FAX)
            .emailClient(UPDATED_EMAIL_CLIENT)
            .typeClient(UPDATED_TYPE_CLIENT)
            .prenomClient(UPDATED_PRENOM_CLIENT)
            .nomCommercial(UPDATED_NOM_COMMERCIAL);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc.perform(put("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNomClient()).isEqualTo(UPDATED_NOM_CLIENT);
        assertThat(testClient.getVilleClient()).isEqualTo(UPDATED_VILLE_CLIENT);
        assertThat(testClient.getAdresseClient()).isEqualTo(UPDATED_ADRESSE_CLIENT);
        assertThat(testClient.getTelephoneClient()).isEqualTo(UPDATED_TELEPHONE_CLIENT);
        assertThat(testClient.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testClient.getEmailClient()).isEqualTo(UPDATED_EMAIL_CLIENT);
        assertThat(testClient.getTypeClient()).isEqualTo(UPDATED_TYPE_CLIENT);
        assertThat(testClient.getPrenomClient()).isEqualTo(UPDATED_PRENOM_CLIENT);
        assertThat(testClient.getNomCommercial()).isEqualTo(UPDATED_NOM_COMMERCIAL);

        // Validate the Client in Elasticsearch
        Client clientEs = clientSearchRepository.findOne(testClient.getId());
        assertThat(clientEs).isEqualToIgnoringGivenFields(testClient);
    }

    @Test
    @Transactional
    public void updateNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClientMockMvc.perform(put("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        clientSearchRepository.save(client);
        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Get the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean clientExistsInEs = clientSearchRepository.exists(client.getId());
        assertThat(clientExistsInEs).isFalse();

        // Validate the database is empty
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        clientSearchRepository.save(client);

        // Search the client
        restClientMockMvc.perform(get("/api/_search/clients?query=id:" + client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomClient").value(hasItem(DEFAULT_NOM_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].villeClient").value(hasItem(DEFAULT_VILLE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].adresseClient").value(hasItem(DEFAULT_ADRESSE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].telephoneClient").value(hasItem(DEFAULT_TELEPHONE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX.toString())))
            .andExpect(jsonPath("$.[*].emailClient").value(hasItem(DEFAULT_EMAIL_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].typeClient").value(hasItem(DEFAULT_TYPE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].prenomClient").value(hasItem(DEFAULT_PRENOM_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].nomCommercial").value(hasItem(DEFAULT_NOM_COMMERCIAL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = new Client();
        client1.setId(1L);
        Client client2 = new Client();
        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);
        client2.setId(2L);
        assertThat(client1).isNotEqualTo(client2);
        client1.setId(null);
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientDTO.class);
        ClientDTO clientDTO1 = new ClientDTO();
        clientDTO1.setId(1L);
        ClientDTO clientDTO2 = new ClientDTO();
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
        clientDTO2.setId(clientDTO1.getId());
        assertThat(clientDTO1).isEqualTo(clientDTO2);
        clientDTO2.setId(2L);
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
        clientDTO1.setId(null);
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(clientMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(clientMapper.fromId(null)).isNull();
    }
}
