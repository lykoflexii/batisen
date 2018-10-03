package com.gesbtp.atos.service;

import com.gesbtp.atos.domain.Chantier;
import com.gesbtp.atos.repository.ChantierRepository;
import com.gesbtp.atos.repository.search.ChantierSearchRepository;
import com.gesbtp.atos.service.dto.ChantierDTO;
import com.gesbtp.atos.service.mapper.ChantierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing Chantier.
 */
@Service
@Transactional
public class ChantierService {

    private final Logger log = LoggerFactory.getLogger(ChantierService.class);

    private final ChantierRepository chantierRepository;

    private final ChantierMapper chantierMapper;

    private final ChantierSearchRepository chantierSearchRepository;

    public ChantierService(ChantierRepository chantierRepository, ChantierMapper chantierMapper, ChantierSearchRepository chantierSearchRepository) {
        this.chantierRepository = chantierRepository;
        this.chantierMapper = chantierMapper;
        this.chantierSearchRepository = chantierSearchRepository;
    }

    /**
     * Save a chantier.
     *
     * @param chantierDTO the entity to save
     * @return the persisted entity
     */
    public ChantierDTO save(ChantierDTO chantierDTO) {
        log.debug("Request to save Chantier : {}", chantierDTO);
        Chantier chantier = chantierMapper.toEntity(chantierDTO);
        chantier = chantierRepository.save(chantier);
        ChantierDTO result = chantierMapper.toDto(chantier);
        chantierSearchRepository.save(chantier);
        return result;
    }

    /**
     * Get all the chantiers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChantierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Chantiers");
        return chantierRepository.findAll(pageable)
            .map(chantierMapper::toDto);
    }

    /**
     * Get one chantier by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ChantierDTO findOne(Long id) {
        log.debug("Request to get Chantier : {}", id);
        Chantier chantier = chantierRepository.findOne(id);
        return chantierMapper.toDto(chantier);
    }

    /**
     * Delete the chantier by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Chantier : {}", id);
        chantierRepository.delete(id);
        chantierSearchRepository.delete(id);
    }

    /**
     * Search for the chantier corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChantierDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Chantiers for query {}", query);
        Page<Chantier> result = chantierSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(chantierMapper::toDto);
    }

    /**
     * Get all the chantiers.
     *
     * @param id the id of the entity
     * @return the list of entities
     * 
     */
    @Transactional(readOnly = true)
    public List<Chantier> findAll(Long id) {
        log.debug("Request to get all Chantiers");
        return chantierRepository.findByClientId(id);
    }

    /**
     * Get one chantier by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<Chantier> findChantierEntrprise(Long id) {
        log.debug("Request to get Chantier : {}", id);
        return chantierRepository.findChantierEntrprise(id);
    }

    /**
     * Get all the chantiers.
     *
     * 
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Chantier> findByUserIsCurrentUser() {
        log.debug("Request to get all Chantiers");
        return chantierRepository.findByUserIsCurrentUser();

    }

    /**
     * Get all the chantiers.
     *
     * @param id the id of the entity
     * @return the list of entities
     * 
     */
    @Transactional(readOnly = true)
    public List<Chantier> findByUserIsCurrentUserAndClient(Long id) {
        log.debug("Request to get all Chantiers");
        return chantierRepository.findByUserIsCurrentUserAndClient(id);
    }
    
    /**
     * Get all the chantiers.
     *
     * @param id the id of the entity
     * @return the list of entities
     * 
     */
    @Transactional(readOnly = true)
    public Integer   chantierTerminer(Long id) {
        log.debug("Request to get all Chantiers");
        return chantierRepository.chantierTerminer(id);
    }
    
    
    /**
     * Get all the chantiers.
     *
     * @param id the id of the entity
     * @return the list of entities
     * 
     */
    @Transactional(readOnly = true)
    public Integer   chantierEnCours(Long id) {
        log.debug("Request to get all Chantiers");
        return chantierRepository.chantierEnCours(id);
    }

     /**
     * Get all the chantiers.
     *
     * @param id the id of the entity
     * @return the list of entities
     * 
     */
    @Transactional(readOnly = true)
    public Integer   chantierEnRetard(Long id) {
        log.debug("Request to get all Chantiers");
        return chantierRepository.chantierEnRetard(id);
    }

     /**
     * Get all the chantiers.
     *
     * @param id the id of the entity
     * @return the list of entities
     * 
     */
    @Transactional(readOnly = true)
    public Integer   chantierEnRetardParClient(Long id) {
        log.debug("Request to get all Chantiers");
        return chantierRepository.chantierEnRetardParClient(id);
    }
    
     /**
     * Get all the chantiers.
     *
     * @param id the id of the entity
     * @return the list of entities
     * 
     */
    @Transactional(readOnly = true)
    public Integer   chantierTerminerParClient(Long id) {
        log.debug("Request to get all Chantiers");
        return chantierRepository.chantierTerminerParClient(id);
    }

    /**
     * Get all the chantiers.
     *
     * @param id the id of the entity
     * @return the list of entities
     * 
     */
    @Transactional(readOnly = true)
    public Integer   chantierEnCoursParClient(Long id) {
        log.debug("Request to get all Chantiers");
        return chantierRepository.chantierEnCoursParClient(id);
    }

}
