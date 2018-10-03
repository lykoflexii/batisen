package com.gesbtp.atos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gesbtp.atos.domain.LigneFacture;
import com.gesbtp.atos.service.LigneFactureService;
import com.gesbtp.atos.web.rest.errors.BadRequestAlertException;
import com.gesbtp.atos.web.rest.util.HeaderUtil;
import com.gesbtp.atos.web.rest.util.PaginationUtil;
import com.gesbtp.atos.service.dto.LigneFactureDTO;
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
 * REST controller for managing LigneFacture.
 */
@RestController
@RequestMapping("/api")
public class LigneFactureResource {

    private final Logger log = LoggerFactory.getLogger(LigneFactureResource.class);

    private static final String ENTITY_NAME = "ligneFacture";

    private final LigneFactureService ligneFactureService;

    public LigneFactureResource(LigneFactureService ligneFactureService) {
        this.ligneFactureService = ligneFactureService;
    }

    /**
     * POST  /ligne-factures : Create a new ligneFacture.
     *
     * @param ligneFactureDTO the ligneFactureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneFactureDTO, or with status 400 (Bad Request) if the ligneFacture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ligne-factures")
    @Timed
    public ResponseEntity<LigneFactureDTO> createLigneFacture(@Valid @RequestBody LigneFactureDTO ligneFactureDTO) throws URISyntaxException {
        log.debug("REST request to save LigneFacture : {}", ligneFactureDTO);
        if (ligneFactureDTO.getId() != null) {
            throw new BadRequestAlertException("A new ligneFacture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigneFactureDTO result = ligneFactureService.save(ligneFactureDTO);
        return ResponseEntity.created(new URI("/api/ligne-factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligne-factures : Updates an existing ligneFacture.
     *
     * @param ligneFactureDTO the ligneFactureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneFactureDTO,
     * or with status 400 (Bad Request) if the ligneFactureDTO is not valid,
     * or with status 500 (Internal Server Error) if the ligneFactureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ligne-factures")
    @Timed
    public ResponseEntity<LigneFactureDTO> updateLigneFacture(@Valid @RequestBody LigneFactureDTO ligneFactureDTO) throws URISyntaxException {
        log.debug("REST request to update LigneFacture : {}", ligneFactureDTO);
        if (ligneFactureDTO.getId() == null) {
            return createLigneFacture(ligneFactureDTO);
        }
        LigneFactureDTO result = ligneFactureService.save(ligneFactureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ligneFactureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligne-factures : get all the ligneFactures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ligneFactures in body
     */
    @GetMapping("/ligne-factures")
    @Timed
    public ResponseEntity<List<LigneFactureDTO>> getAllLigneFactures(Pageable pageable) {
        log.debug("REST request to get a page of LigneFactures");
        Page<LigneFactureDTO> page = ligneFactureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ligne-factures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligne-factures/:id : get the "id" ligneFacture.
     *
     * @param id the id of the ligneFactureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneFactureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ligne-factures/{id}")
    @Timed
    public ResponseEntity<LigneFactureDTO> getLigneFacture(@PathVariable Long id) {
        log.debug("REST request to get LigneFacture : {}", id);
        LigneFactureDTO ligneFactureDTO = ligneFactureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ligneFactureDTO));
    }

    /**
     * DELETE  /ligne-factures/:id : delete the "id" ligneFacture.
     *
     * @param id the id of the ligneFactureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ligne-factures/{id}")
    @Timed
    public ResponseEntity<Void> deleteLigneFacture(@PathVariable Long id) {
        log.debug("REST request to delete LigneFacture : {}", id);
        ligneFactureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligne-factures?query=:query : search for the ligneFacture corresponding
     * to the query.
     *
     * @param query the query of the ligneFacture search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ligne-factures")
    @Timed
    public ResponseEntity<List<LigneFactureDTO>> searchLigneFactures(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LigneFactures for query {}", query);
        Page<LigneFactureDTO> page = ligneFactureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ligne-factures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligne-devis/:id : get the "id" ligneDevis.
     *
     * @param id the id of the ligneDevisDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneDevisDTO, or with status 404 (Not Found)
     */
    @GetMapping("/parFacture/ligne-factures/{id}")
    @Timed
    public ResponseEntity<List<LigneFacture>> ligneFactureByFcatureId(@PathVariable Long id) {
        log.debug("REST request to get LigneDevis : {}", id);
       List<LigneFacture> ligneFacture = ligneFactureService.ligneFactureByFactureId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ligneFacture));
    }
}
