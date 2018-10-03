package com.gesbtp.atos.service;

import com.gesbtp.atos.domain.LigneFacture;
import com.gesbtp.atos.repository.LigneFactureRepository;
import com.gesbtp.atos.repository.search.LigneFactureSearchRepository;
import com.gesbtp.atos.service.dto.LigneFactureDTO;
import com.gesbtp.atos.service.mapper.LigneFactureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing LigneFacture.
 */
@Service
@Transactional
public class LigneFactureService {

    private final Logger log = LoggerFactory.getLogger(LigneFactureService.class);

    private final LigneFactureRepository ligneFactureRepository;

    private final LigneFactureMapper ligneFactureMapper;

    private final LigneFactureSearchRepository ligneFactureSearchRepository;

    public LigneFactureService(LigneFactureRepository ligneFactureRepository, LigneFactureMapper ligneFactureMapper, LigneFactureSearchRepository ligneFactureSearchRepository) {
        this.ligneFactureRepository = ligneFactureRepository;
        this.ligneFactureMapper = ligneFactureMapper;
        this.ligneFactureSearchRepository = ligneFactureSearchRepository;
    }

    /**
     * Save a ligneFacture.
     *
     * @param ligneFactureDTO the entity to save
     * @return the persisted entity
     */
    public LigneFactureDTO save(LigneFactureDTO ligneFactureDTO) {
        log.debug("Request to save LigneFacture : {}", ligneFactureDTO);
        LigneFacture ligneFacture = ligneFactureMapper.toEntity(ligneFactureDTO);
        ligneFacture = ligneFactureRepository.save(ligneFacture);
        LigneFactureDTO result = ligneFactureMapper.toDto(ligneFacture);
        ligneFactureSearchRepository.save(ligneFacture);
        return result;
    }

    /**
     * Get all the ligneFactures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneFactureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LigneFactures");
        return ligneFactureRepository.findAll(pageable)
            .map(ligneFactureMapper::toDto);
    }

    /**
     * Get one ligneFacture by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public LigneFactureDTO findOne(Long id) {
        log.debug("Request to get LigneFacture : {}", id);
        LigneFacture ligneFacture = ligneFactureRepository.findOne(id);
        return ligneFactureMapper.toDto(ligneFacture);
    }

    /**
     * Delete the ligneFacture by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneFacture : {}", id);
        ligneFactureRepository.delete(id);
        ligneFactureSearchRepository.delete(id);
    }

    /**
     * Search for the ligneFacture corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneFactureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LigneFactures for query {}", query);
        Page<LigneFacture> result = ligneFactureSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(ligneFactureMapper::toDto);
    }

    /**
     * Get one ligneFacture by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<LigneFacture> ligneFactureByFactureId(Long id) {
        log.debug("Request to get all LigneFacture");
        return ligneFactureRepository.ligneFactureByFactureId(id);
        }
}
