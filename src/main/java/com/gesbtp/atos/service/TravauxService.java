package com.gesbtp.atos.service;

import com.gesbtp.atos.domain.Travaux;
import com.gesbtp.atos.repository.TravauxRepository;
import com.gesbtp.atos.repository.search.TravauxSearchRepository;
import com.gesbtp.atos.service.dto.TravauxDTO;
import com.gesbtp.atos.service.mapper.TravauxMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Travaux.
 */
@Service
@Transactional
public class TravauxService {

    private final Logger log = LoggerFactory.getLogger(TravauxService.class);

    private final TravauxRepository travauxRepository;

    private final TravauxMapper travauxMapper;

    private final TravauxSearchRepository travauxSearchRepository;

    public TravauxService(TravauxRepository travauxRepository, TravauxMapper travauxMapper, TravauxSearchRepository travauxSearchRepository) {
        this.travauxRepository = travauxRepository;
        this.travauxMapper = travauxMapper;
        this.travauxSearchRepository = travauxSearchRepository;
    }

    /**
     * Save a travaux.
     *
     * @param travauxDTO the entity to save
     * @return the persisted entity
     */
    public TravauxDTO save(TravauxDTO travauxDTO) {
        log.debug("Request to save Travaux : {}", travauxDTO);
        Travaux travaux = travauxMapper.toEntity(travauxDTO);
        travaux = travauxRepository.save(travaux);
        TravauxDTO result = travauxMapper.toDto(travaux);
        travauxSearchRepository.save(travaux);
        return result;
    }

    /**
     * Get all the travauxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravauxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Travauxes");
        return travauxRepository.findAll(pageable)
            .map(travauxMapper::toDto);
    }

    /**
     * Get one travaux by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TravauxDTO findOne(Long id) {
        log.debug("Request to get Travaux : {}", id);
        Travaux travaux = travauxRepository.findOne(id);
        return travauxMapper.toDto(travaux);
    }

    /**
     * Delete the travaux by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Travaux : {}", id);
        travauxRepository.delete(id);
        travauxSearchRepository.delete(id);
    }

    /**
     * Search for the travaux corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravauxDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Travauxes for query {}", query);
        Page<Travaux> result = travauxSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(travauxMapper::toDto);
    }
}
