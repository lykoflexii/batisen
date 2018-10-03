package com.gesbtp.atos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gesbtp.atos.domain.LigneDevis;
import com.gesbtp.atos.service.LigneDevisService;
import com.gesbtp.atos.web.rest.errors.BadRequestAlertException;
import com.gesbtp.atos.web.rest.util.HeaderUtil;
import com.gesbtp.atos.web.rest.util.PaginationUtil;
import com.gesbtp.atos.service.dto.LigneDevisDTO;
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
 * REST controller for managing LigneDevis.
 */
@RestController
@RequestMapping("/api")
public class LigneDevisResource {

    private final Logger log = LoggerFactory.getLogger(LigneDevisResource.class);

    private static final String ENTITY_NAME = "ligneDevis";

    private final LigneDevisService ligneDevisService;

    public LigneDevisResource(LigneDevisService ligneDevisService) {
        this.ligneDevisService = ligneDevisService;
    }

    /**
     * POST  /ligne-devis : Create a new ligneDevis.
     *
     * @param ligneDevisDTO the ligneDevisDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneDevisDTO, or with status 400 (Bad Request) if the ligneDevis has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ligne-devis")
    @Timed
    public ResponseEntity<LigneDevisDTO> createLigneDevis(@Valid @RequestBody LigneDevisDTO ligneDevisDTO) throws URISyntaxException {
        log.debug("REST request to save LigneDevis : {}", ligneDevisDTO);
        if (ligneDevisDTO.getId() != null) {
            throw new BadRequestAlertException("A new ligneDevis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigneDevisDTO result = ligneDevisService.save(ligneDevisDTO);
        return ResponseEntity.created(new URI("/api/ligne-devis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligne-devis : Updates an existing ligneDevis.
     *
     * @param ligneDevisDTO the ligneDevisDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneDevisDTO,
     * or with status 400 (Bad Request) if the ligneDevisDTO is not valid,
     * or with status 500 (Internal Server Error) if the ligneDevisDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ligne-devis")
    @Timed
    public ResponseEntity<LigneDevisDTO> updateLigneDevis(@Valid @RequestBody LigneDevisDTO ligneDevisDTO) throws URISyntaxException {
        log.debug("REST request to update LigneDevis : {}", ligneDevisDTO);
        if (ligneDevisDTO.getId() == null) {
            return createLigneDevis(ligneDevisDTO);
        }
        LigneDevisDTO result = ligneDevisService.save(ligneDevisDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ligneDevisDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligne-devis : get all the ligneDevis.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ligneDevis in body
     */
    @GetMapping("/ligne-devis")
    @Timed
    public ResponseEntity<List<LigneDevisDTO>> getAllLigneDevis(Pageable pageable) {
        log.debug("REST request to get a page of LigneDevis");
        Page<LigneDevisDTO> page = ligneDevisService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ligne-devis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligne-devis/:id : get the "id" ligneDevis.
     *
     * @param id the id of the ligneDevisDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneDevisDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ligne-devis/{id}")
    @Timed
    public ResponseEntity<LigneDevisDTO> getLigneDevis(@PathVariable Long id) {
        log.debug("REST request to get LigneDevis : {}", id);
        LigneDevisDTO ligneDevisDTO = ligneDevisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ligneDevisDTO));
    }

    /**
     * DELETE  /ligne-devis/:id : delete the "id" ligneDevis.
     *
     * @param id the id of the ligneDevisDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ligne-devis/{id}")
    @Timed
    public ResponseEntity<Void> deleteLigneDevis(@PathVariable Long id) {
        log.debug("REST request to delete LigneDevis : {}", id);
        ligneDevisService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligne-devis?query=:query : search for the ligneDevis corresponding
     * to the query.
     *
     * @param query the query of the ligneDevis search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ligne-devis")
    @Timed
    public ResponseEntity<List<LigneDevisDTO>> searchLigneDevis(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LigneDevis for query {}", query);
        Page<LigneDevisDTO> page = ligneDevisService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ligne-devis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /ligne-devis/:id : get the "id" ligneDevis.
     *
     * @param id the id of the ligneDevisDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneDevisDTO, or with status 404 (Not Found)
     */
    @GetMapping("/parDevis/ligne-devis/{id}")
    @Timed
    public ResponseEntity<List<LigneDevis>> ligneDevisByDevisId(@PathVariable Long id) {
        log.debug("REST request to get LigneDevis : {}", id);
       List<LigneDevis> ligneDevis = ligneDevisService.ligneDevisByDevisId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ligneDevis));
    }

}
