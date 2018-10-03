package com.gesbtp.atos.service;

import com.gesbtp.atos.domain.Employe;
import com.gesbtp.atos.repository.EmployeRepository;
import com.gesbtp.atos.repository.search.EmployeSearchRepository;
import com.gesbtp.atos.service.dto.EmployeDTO;
import com.gesbtp.atos.service.mapper.EmployeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing Employe.
 */
@Service
@Transactional
public class EmployeService {

    private final Logger log = LoggerFactory.getLogger(EmployeService.class);

    private final EmployeRepository employeRepository;

    private final EmployeMapper employeMapper;

    private final EmployeSearchRepository employeSearchRepository;

    public EmployeService(EmployeRepository employeRepository, EmployeMapper employeMapper, EmployeSearchRepository employeSearchRepository) {
        this.employeRepository = employeRepository;
        this.employeMapper = employeMapper;
        this.employeSearchRepository = employeSearchRepository;
    }

    /**
     * Save a employe.
     *
     * @param employeDTO the entity to save
     * @return the persisted entity
     */
    public EmployeDTO save(EmployeDTO employeDTO) {
        log.debug("Request to save Employe : {}", employeDTO);
        Employe employe = employeMapper.toEntity(employeDTO);
        employe = employeRepository.save(employe);
        EmployeDTO result = employeMapper.toDto(employe);
        employeSearchRepository.save(employe);
        return result;
    }

    /**
     * Get all the employes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmployeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employes");
        return employeRepository.findAll(pageable)
            .map(employeMapper::toDto);
    }

    /**
     * Get one employe by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public EmployeDTO findOne(Long id) {
        log.debug("Request to get Employe : {}", id);
        Employe employe = employeRepository.findOne(id);
        return employeMapper.toDto(employe);
    }

    /**
     * Delete the employe by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Employe : {}", id);
        employeRepository.delete(id);
        employeSearchRepository.delete(id);
    }

    /**
     * Search for the employe corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmployeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Employes for query {}", query);
        Page<Employe> result = employeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(employeMapper::toDto);
    }

     /**
     * Get one client by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<Employe> findEntrepriseEmploye(Long id) {
        log.debug("Request to get Employe : {}", id);
       return employeRepository.findEntrepriseEmploye(id);
    }
}
