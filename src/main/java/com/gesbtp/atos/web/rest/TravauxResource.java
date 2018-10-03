package com.gesbtp.atos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gesbtp.atos.service.TravauxService;
import com.gesbtp.atos.web.rest.errors.BadRequestAlertException;
import com.gesbtp.atos.web.rest.util.HeaderUtil;
import com.gesbtp.atos.web.rest.util.PaginationUtil;
import com.gesbtp.atos.service.dto.TravauxDTO;
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
 * REST controller for managing Travaux.
 */
@RestController
@RequestMapping("/api")
public class TravauxResource {

    private final Logger log = LoggerFactory.getLogger(TravauxResource.class);

    private static final String ENTITY_NAME = "travaux";

    private final TravauxService travauxService;

    public TravauxResource(TravauxService travauxService) {
        this.travauxService = travauxService;
    }

    /**
     * POST  /travauxes : Create a new travaux.
     *
     * @param travauxDTO the travauxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new travauxDTO, or with status 400 (Bad Request) if the travaux has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/travauxes")
    @Timed
    public ResponseEntity<TravauxDTO> createTravaux(@Valid @RequestBody TravauxDTO travauxDTO) throws URISyntaxException {
        log.debug("REST request to save Travaux : {}", travauxDTO);
        if (travauxDTO.getId() != null) {
            throw new BadRequestAlertException("A new travaux cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TravauxDTO result = travauxService.save(travauxDTO);
        return ResponseEntity.created(new URI("/api/travauxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /travauxes : Updates an existing travaux.
     *
     * @param travauxDTO the travauxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travauxDTO,
     * or with status 400 (Bad Request) if the travauxDTO is not valid,
     * or with status 500 (Internal Server Error) if the travauxDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/travauxes")
    @Timed
    public ResponseEntity<TravauxDTO> updateTravaux(@Valid @RequestBody TravauxDTO travauxDTO) throws URISyntaxException {
        log.debug("REST request to update Travaux : {}", travauxDTO);
        if (travauxDTO.getId() == null) {
            return createTravaux(travauxDTO);
        }
        TravauxDTO result = travauxService.save(travauxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, travauxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /travauxes : get all the travauxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of travauxes in body
     */
    @GetMapping("/travauxes")
    @Timed
    public ResponseEntity<List<TravauxDTO>> getAllTravauxes(Pageable pageable) {
        log.debug("REST request to get a page of Travauxes");
        Page<TravauxDTO> page = travauxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/travauxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /travauxes/:id : get the "id" travaux.
     *
     * @param id the id of the travauxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the travauxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/travauxes/{id}")
    @Timed
    public ResponseEntity<TravauxDTO> getTravaux(@PathVariable Long id) {
        log.debug("REST request to get Travaux : {}", id);
        TravauxDTO travauxDTO = travauxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(travauxDTO));
    }

    /**
     * DELETE  /travauxes/:id : delete the "id" travaux.
     *
     * @param id the id of the travauxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/travauxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTravaux(@PathVariable Long id) {
        log.debug("REST request to delete Travaux : {}", id);
        travauxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/travauxes?query=:query : search for the travaux corresponding
     * to the query.
     *
     * @param query the query of the travaux search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/travauxes")
    @Timed
    public ResponseEntity<List<TravauxDTO>> searchTravauxes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Travauxes for query {}", query);
        Page<TravauxDTO> page = travauxService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/travauxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
