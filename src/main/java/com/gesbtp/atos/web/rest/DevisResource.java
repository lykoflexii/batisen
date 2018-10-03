package com.gesbtp.atos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gesbtp.atos.domain.Devis;
import com.gesbtp.atos.service.DevisService;
import com.gesbtp.atos.web.rest.errors.BadRequestAlertException;
import com.gesbtp.atos.web.rest.util.HeaderUtil;
import com.gesbtp.atos.web.rest.util.PaginationUtil;
import com.gesbtp.atos.service.dto.DevisDTO;
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
 * REST controller for managing Devis.
 */
@RestController
@RequestMapping("/api")
public class DevisResource {

    private final Logger log = LoggerFactory.getLogger(DevisResource.class);

    private static final String ENTITY_NAME = "devis";

    private final DevisService devisService;

    public DevisResource(DevisService devisService) {
        this.devisService = devisService;
    }

    /**
     * POST  /devis : Create a new devis.
     *
     * @param devisDTO the devisDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new devisDTO, or with status 400 (Bad Request) if the devis has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/devis")
    @Timed
    public ResponseEntity<DevisDTO> createDevis(@Valid @RequestBody DevisDTO devisDTO) throws URISyntaxException {
        log.debug("REST request to save Devis : {}", devisDTO);
        if (devisDTO.getId() != null) {
            throw new BadRequestAlertException("A new devis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DevisDTO result = devisService.save(devisDTO);
        return ResponseEntity.created(new URI("/api/devis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /devis : Updates an existing devis.
     *
     * @param devisDTO the devisDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated devisDTO,
     * or with status 400 (Bad Request) if the devisDTO is not valid,
     * or with status 500 (Internal Server Error) if the devisDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/devis")
    @Timed
    public ResponseEntity<DevisDTO> updateDevis(@Valid @RequestBody DevisDTO devisDTO) throws URISyntaxException {
        log.debug("REST request to update Devis : {}", devisDTO);
        if (devisDTO.getId() == null) {
            return createDevis(devisDTO);
        }
        DevisDTO result = devisService.save(devisDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, devisDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /devis : get all the devis.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of devis in body
     */
    @GetMapping("/devis")
    @Timed
    public ResponseEntity<List<DevisDTO>> getAllDevis(Pageable pageable) {
        log.debug("REST request to get a page of Devis");
        Page<DevisDTO> page = devisService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/devis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /devis/:id : get the "id" devis.
     *
     * @param id the id of the devisDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the devisDTO, or with status 404 (Not Found)
     */
    @GetMapping("/devis/{id}")
    @Timed
    public ResponseEntity<DevisDTO> getDevis(@PathVariable Long id) {
        log.debug("REST request to get Devis : {}", id);
        DevisDTO devisDTO = devisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(devisDTO));
    }

    /**
     * DELETE  /devis/:id : delete the "id" devis.
     *
     * @param id the id of the devisDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/devis/{id}")
    @Timed
    public ResponseEntity<Void> deleteDevis(@PathVariable Long id) {
        log.debug("REST request to delete Devis : {}", id);
        devisService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/devis?query=:query : search for the devis corresponding
     * to the query.
     *
     * @param query the query of the devis search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/devis")
    @Timed
    public ResponseEntity<List<DevisDTO>> searchDevis(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Devis for query {}", query);
        Page<DevisDTO> page = devisService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/devis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clients/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/filter/devis/{id}")
    @Timed
    public ResponseEntity<List<Devis>> getDevisEntreprise(@PathVariable Long id) {
        log.debug("REST request to get Devis : {}", id);
        List<Devis> devis = devisService.DevisEntreprise(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(devis));
    }

    /**
     * GET  /clients/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/last/devis")
    @Timed
    public Long getLasteId() {
        
        return devisService.lastId();
    }

    /**
     * GET  /clients/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cinqDernier/devis/{id}")
    @Timed
    public ResponseEntity<List<Devis>> getcinqDerniersDevis(@PathVariable Long id) {
        log.debug("REST request to get Devis : {}", id);
        List<Devis> devis = devisService.cinqDerniersDevis(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(devis));
    }

}
