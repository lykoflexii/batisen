package com.gesbtp.atos.service;

import com.gesbtp.atos.domain.Devis;
import com.gesbtp.atos.repository.DevisRepository;
import com.gesbtp.atos.repository.search.DevisSearchRepository;
import com.gesbtp.atos.service.dto.DevisDTO;
import com.gesbtp.atos.service.mapper.DevisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing Devis.
 */
@Service
@Transactional
public class DevisService {

    private final Logger log = LoggerFactory.getLogger(DevisService.class);

    private final DevisRepository devisRepository;

    private final DevisMapper devisMapper;

    private final DevisSearchRepository devisSearchRepository;

    public DevisService(DevisRepository devisRepository, DevisMapper devisMapper, DevisSearchRepository devisSearchRepository) {
        this.devisRepository = devisRepository;
        this.devisMapper = devisMapper;
        this.devisSearchRepository = devisSearchRepository;
    }

    /**
     * Save a devis.
     *
     * @param devisDTO the entity to save
     * @return the persisted entity
     */
    public DevisDTO save(DevisDTO devisDTO) {
        log.debug("Request to save Devis : {}", devisDTO);
        Devis devis = devisMapper.toEntity(devisDTO);
        devis = devisRepository.save(devis);
        DevisDTO result = devisMapper.toDto(devis);
        devisSearchRepository.save(devis);
        return result;
    }

    /**
     * Get all the devis.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DevisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Devis");
        return devisRepository.findAll(pageable)
            .map(devisMapper::toDto);
    }

    /**
     * Get one devis by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public DevisDTO findOne(Long id) {
        log.debug("Request to get Devis : {}", id);
        Devis devis = devisRepository.findOne(id);
        return devisMapper.toDto(devis);
    }

    /**
     * Delete the devis by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Devis : {}", id);
        devisRepository.delete(id);
        devisSearchRepository.delete(id);
    }

    /**
     * Search for the devis corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DevisDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Devis for query {}", query);
        Page<Devis> result = devisSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(devisMapper::toDto);
    }

    @Transactional(readOnly = true)
    public DevisDTO lastDevist() {
        Devis devis = devisRepository.dernierDevis();
        return devisMapper.toDto(devis);
    }

    @Transactional(readOnly = true)
    public Long lastId() {
        return devisRepository.dernierId();
    }

    @Transactional(readOnly = true)
    public List<Devis> DevisByClientId(Long id) {
        return devisRepository.DevisByClientId(id);
        
    }

    @Transactional(readOnly = true)
    public Devis DevisByChantierId(Long id) {
        return devisRepository.DevisByChantierId(id);
        
    }

     /**
     * Get one client by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<Devis> DevisEntreprise(Long id) {
        log.debug("Request to get Devis : {}", id);
       return devisRepository.DevisEntreprise(id);
    }

    
     /**
     * Get one client by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<Devis> cinqDerniersDevis(Long id) {
        log.debug("Request to get Devis : {}", id);
       return devisRepository.cinqDerniersDevis(id);
    }
}
