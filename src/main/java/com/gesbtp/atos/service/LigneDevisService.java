package com.gesbtp.atos.service;

import com.gesbtp.atos.domain.LigneDevis;
import com.gesbtp.atos.repository.LigneDevisRepository;
import com.gesbtp.atos.repository.search.LigneDevisSearchRepository;
import com.gesbtp.atos.service.dto.LigneDevisDTO;
import com.gesbtp.atos.service.mapper.LigneDevisMapper;
import com.thoughtworks.xstream.mapper.Mapper.Null;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing LigneDevis.
 */
@Service
@Transactional
public class LigneDevisService {

    private final Logger log = LoggerFactory.getLogger(LigneDevisService.class);

    private final LigneDevisRepository ligneDevisRepository;

    private final LigneDevisMapper ligneDevisMapper;

    private final LigneDevisSearchRepository ligneDevisSearchRepository;

    public LigneDevisService(LigneDevisRepository ligneDevisRepository, LigneDevisMapper ligneDevisMapper, LigneDevisSearchRepository ligneDevisSearchRepository) {
        this.ligneDevisRepository = ligneDevisRepository;
        this.ligneDevisMapper = ligneDevisMapper;
        this.ligneDevisSearchRepository = ligneDevisSearchRepository;
    }

    /**
     * Save a ligneDevis.
     *
     * @param ligneDevisDTO the entity to save
     * @return the persisted entity
     */
    public LigneDevisDTO save(LigneDevisDTO ligneDevisDTO) {
        log.debug("Request to save LigneDevis : {}", ligneDevisDTO);
        LigneDevis ligneDevis = ligneDevisMapper.toEntity(ligneDevisDTO);
        ligneDevis = ligneDevisRepository.save(ligneDevis);
        LigneDevisDTO result = ligneDevisMapper.toDto(ligneDevis);
        ligneDevisSearchRepository.save(ligneDevis);
        return result;
    }

    /**
     * Get all the ligneDevis.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneDevisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LigneDevis");
        return ligneDevisRepository.findAll(pageable)
            .map(ligneDevisMapper::toDto);
    }

    /**
     * Get one ligneDevis by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public LigneDevisDTO findOne(Long id) {
        log.debug("Request to get LigneDevis : {}", id);
        LigneDevis ligneDevis = ligneDevisRepository.findOne(id);
        return ligneDevisMapper.toDto(ligneDevis);
    }

    /**
     * Delete the ligneDevis by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneDevis : {}", id);
        ligneDevisRepository.delete(id);
        ligneDevisSearchRepository.delete(id);
    }

    /**
     * Search for the ligneDevis corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneDevisDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LigneDevis for query {}", query);
        Page<LigneDevis> result = ligneDevisSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(ligneDevisMapper::toDto);
    }

    /**
     * Get one ligneDevis by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<LigneDevis> ligneDevisByDevisId(Long id) {
        log.debug("Request to get all LigneDevis");
        return ligneDevisRepository.ligneDevisByDevisId(id);
        }

}
