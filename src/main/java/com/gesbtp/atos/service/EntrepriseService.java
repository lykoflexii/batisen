package com.gesbtp.atos.service;

import com.gesbtp.atos.domain.Entreprise;
import com.gesbtp.atos.repository.EntrepriseRepository;
import com.gesbtp.atos.repository.search.EntrepriseSearchRepository;
import com.gesbtp.atos.service.dto.EntrepriseDTO;
import com.gesbtp.atos.service.mapper.EntrepriseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Entreprise.
 */
@Service
@Transactional
public class EntrepriseService {

    private final Logger log = LoggerFactory.getLogger(EntrepriseService.class);

    private final EntrepriseRepository entrepriseRepository;

    private final EntrepriseMapper entrepriseMapper;

    private final EntrepriseSearchRepository entrepriseSearchRepository;

    public EntrepriseService(EntrepriseRepository entrepriseRepository, EntrepriseMapper entrepriseMapper, EntrepriseSearchRepository entrepriseSearchRepository) {
        this.entrepriseRepository = entrepriseRepository;
        this.entrepriseMapper = entrepriseMapper;
        this.entrepriseSearchRepository = entrepriseSearchRepository;
    }

    /**
     * Save a entreprise.
     *
     * @param entrepriseDTO the entity to save
     * @return the persisted entity
     */
    public EntrepriseDTO save(EntrepriseDTO entrepriseDTO) {
        log.debug("Request to save Entreprise : {}", entrepriseDTO);
        Entreprise entreprise = entrepriseMapper.toEntity(entrepriseDTO);
        entreprise = entrepriseRepository.save(entreprise);
        EntrepriseDTO result = entrepriseMapper.toDto(entreprise);
        entrepriseSearchRepository.save(entreprise);
        return result;
    }

    /**
     * Get all the entreprises.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EntrepriseDTO> findAll() {
        log.debug("Request to get all Entreprises");
        return entrepriseRepository.findAll().stream()
            .map(entrepriseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one entreprise by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public EntrepriseDTO findOne(Long id) {
        log.debug("Request to get Entreprise : {}", id);
        Entreprise entreprise = entrepriseRepository.findOne(id);
        return entrepriseMapper.toDto(entreprise);
    }

    /**
     * Delete the entreprise by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Entreprise : {}", id);
        entrepriseRepository.delete(id);
        entrepriseSearchRepository.delete(id);
    }

    /**
     * Search for the entreprise corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EntrepriseDTO> search(String query) {
        log.debug("Request to search Entreprises for query {}", query);
        return StreamSupport
            .stream(entrepriseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(entrepriseMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get all the entreprises.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Entreprise> listeAttente() {
        log.debug("Request to get all Entreprises");
        return entrepriseRepository.listeAttente();
    }
}
