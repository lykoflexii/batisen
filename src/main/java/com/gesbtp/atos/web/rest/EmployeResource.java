package com.gesbtp.atos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gesbtp.atos.domain.Employe;
import com.gesbtp.atos.service.EmployeService;
import com.gesbtp.atos.web.rest.errors.BadRequestAlertException;
import com.gesbtp.atos.web.rest.util.HeaderUtil;
import com.gesbtp.atos.web.rest.util.PaginationUtil;
import com.gesbtp.atos.service.dto.EmployeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Employe.
 */
@RestController
@RequestMapping("/api")
public class EmployeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeResource.class);

    private static final String ENTITY_NAME = "employe";

    private final EmployeService employeService;

    public EmployeResource(EmployeService employeService) {
        this.employeService = employeService;
    }

    /**
     * POST  /employes : Create a new employe.
     *
     * @param employeDTO the employeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeDTO, or with status 400 (Bad Request) if the employe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/employes")
    @Timed
    public ResponseEntity<EmployeDTO> createEmploye(@Valid @RequestBody EmployeDTO employeDTO) throws URISyntaxException {
        log.debug("REST request to save Employe : {}", employeDTO);
        if (employeDTO.getId() != null) {
            throw new BadRequestAlertException("A new employe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeDTO result = employeService.save(employeDTO);
        return ResponseEntity.created(new URI("/api/employes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employes : Updates an existing employe.
     *
     * @param employeDTO the employeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeDTO,
     * or with status 400 (Bad Request) if the employeDTO is not valid,
     * or with status 500 (Internal Server Error) if the employeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/employes")
    @Timed
    public ResponseEntity<EmployeDTO> updateEmploye(@Valid @RequestBody EmployeDTO employeDTO) throws URISyntaxException {
        log.debug("REST request to update Employe : {}", employeDTO);
        if (employeDTO.getId() == null) {
            return createEmploye(employeDTO);
        }
        EmployeDTO result = employeService.save(employeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, employeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employes : get all the employes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of employes in body
     */
    @GetMapping("/employes")
    @Timed
    public ResponseEntity<List<EmployeDTO>> getAllEmployes(Pageable pageable) {
        log.debug("REST request to get a page of Employes");
        Page<EmployeDTO> page = employeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /employes/:id : get the "id" employe.
     *
     * @param id the id of the employeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/employes/{id}")
    @Timed
    public ResponseEntity<EmployeDTO> getEmploye(@PathVariable Long id) {
        log.debug("REST request to get Employe : {}", id);
        EmployeDTO employeDTO = employeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(employeDTO));
    }

    /**
     * DELETE  /employes/:id : delete the "id" employe.
     *
     * @param id the id of the employeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/employes/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        log.debug("REST request to delete Employe : {}", id);
        employeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/employes?query=:query : search for the employe corresponding
     * to the query.
     *
     * @param query the query of the employe search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/employes")
    @Timed
    public ResponseEntity<List<EmployeDTO>> searchEmployes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Employes for query {}", query);
        Page<EmployeDTO> page = employeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/employes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /Employes/:id : get the "id" Employe.
     *
     * @param id the id of the EmployeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the EmployeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/filter/employes/{id}")
    @Timed
    public ResponseEntity<List<Employe>> getEntrepriseEmploye(@PathVariable Long id) {
        log.debug("REST request to get Employe : {}", id);
        List<Employe> employes = employeService.findEntrepriseEmploye(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(employes));
    }
}
