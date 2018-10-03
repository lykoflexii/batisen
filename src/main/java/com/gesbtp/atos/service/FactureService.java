package com.gesbtp.atos.service;

import com.gesbtp.atos.domain.Facture;
import com.gesbtp.atos.repository.FactureRepository;
import com.gesbtp.atos.repository.search.FactureSearchRepository;
import com.gesbtp.atos.service.dto.FactureDTO;
import com.gesbtp.atos.service.mapper.FactureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing Facture.
 */
@Service
@Transactional
public class FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureService.class);

    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;

    private final FactureSearchRepository factureSearchRepository;

    public FactureService(FactureRepository factureRepository, FactureMapper factureMapper, FactureSearchRepository factureSearchRepository) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
        this.factureSearchRepository = factureSearchRepository;
    }

    /**
     * Save a facture.
     *
     * @param factureDTO the entity to save
     * @return the persisted entity
     */
    public FactureDTO save(FactureDTO factureDTO) {
        log.debug("Request to save Facture : {}", factureDTO);
        Facture facture = factureMapper.toEntity(factureDTO);
        facture = factureRepository.save(facture);
        FactureDTO result = factureMapper.toDto(facture);
        factureSearchRepository.save(facture);
        return result;
    }

    /**
     * Get all the factures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FactureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Factures");
        return factureRepository.findAll(pageable)
            .map(factureMapper::toDto);
    }

    /**
     * Get one facture by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FactureDTO findOne(Long id) {
        log.debug("Request to get Facture : {}", id);
        Facture facture = factureRepository.findOne(id);
        return factureMapper.toDto(facture);
    }

    /**
     * Delete the facture by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.delete(id);
        factureSearchRepository.delete(id);
    }

    /**
     * Search for the facture corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FactureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Factures for query {}", query);
        Page<Facture> result = factureSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(factureMapper::toDto);
    }

    /**
     * Get one facture by id.
     *
     * @param id the id of the entity
     * @return the Long
     */
    @Transactional(readOnly = true)
    public Long lastId(Long id, Long id2) {
        return factureRepository.dernierId(id,id2);
    }

    /**
     * Get one facture by id.
     *
     * @param id the id of the entity
     * @return the Long
     */
    @Transactional(readOnly = true)
    public Facture factureChantierTravaux(Long id, Long id2) {
        return factureRepository.factureChantierTravaux(id,id2);
    }

     /**
     * Get one facture by id.
     *
     * @param id the id of the entity
     * @return the Long
     */
    @Transactional(readOnly = true)
    public List<Facture> factureEntreprise(Long id) {
        return factureRepository.factureEntreprise(id);
    }

     /**
     * Get one facture by id.
     *
     * @param id the id of the entity
     * @return the Long
     */
    @Transactional(readOnly = true)
    public List<Facture> factureValide(Long id) {
        return factureRepository.factureValide(id);
    }

    /**
     * Get one facture by id.
     *
     * @param id the id of the entity
     * @return the Long
     */
    @Transactional(readOnly = true)
    public List<Facture> factureNonValide(Long id) {
        return factureRepository.factureNonValide(id);
    }

     /**
     * Get one facture by id.
     *
     * @param id the id of the entity
     * @return the Long
     */
    @Transactional(readOnly = true)
    public List<Facture> factureChantierTravauxValider(Long id, Long id2) {
        return factureRepository.factureChantierTravauxValider(id,id2);
    }

}
