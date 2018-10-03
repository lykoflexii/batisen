package com.gesbtp.atos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gesbtp.atos.domain.Facture;
import com.gesbtp.atos.service.FactureService;
import com.gesbtp.atos.web.rest.errors.BadRequestAlertException;
import com.gesbtp.atos.web.rest.util.HeaderUtil;
import com.gesbtp.atos.web.rest.util.PaginationUtil;
import com.gesbtp.atos.service.dto.FactureDTO;
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
 * REST controller for managing Facture.
 */
@RestController
@RequestMapping("/api")
public class FactureResource {

    private final Logger log = LoggerFactory.getLogger(FactureResource.class);

    private static final String ENTITY_NAME = "facture";

    private final FactureService factureService;

    public FactureResource(FactureService factureService) {
        this.factureService = factureService;
    }

    /**
     * POST  /factures : Create a new facture.
     *
     * @param factureDTO the factureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new factureDTO, or with status 400 (Bad Request) if the facture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/factures")
    @Timed
    public ResponseEntity<FactureDTO> createFacture(@Valid @RequestBody FactureDTO factureDTO) throws URISyntaxException {
        log.debug("REST request to save Facture : {}", factureDTO);
        if (factureDTO.getId() != null) {
            throw new BadRequestAlertException("A new facture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FactureDTO result = factureService.save(factureDTO);
        return ResponseEntity.created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /factures : Updates an existing facture.
     *
     * @param factureDTO the factureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated factureDTO,
     * or with status 400 (Bad Request) if the factureDTO is not valid,
     * or with status 500 (Internal Server Error) if the factureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/factures")
    @Timed
    public ResponseEntity<FactureDTO> updateFacture(@Valid @RequestBody FactureDTO factureDTO) throws URISyntaxException {
        log.debug("REST request to update Facture : {}", factureDTO);
        if (factureDTO.getId() == null) {
            return createFacture(factureDTO);
        }
        FactureDTO result = factureService.save(factureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, factureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /factures : get all the factures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of factures in body
     */
    @GetMapping("/factures")
    @Timed
    public ResponseEntity<List<FactureDTO>> getAllFactures(Pageable pageable) {
        log.debug("REST request to get a page of Factures");
        Page<FactureDTO> page = factureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/factures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /factures/:id : get the "id" facture.
     *
     * @param id the id of the factureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the factureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/factures/{id}")
    @Timed
    public ResponseEntity<FactureDTO> getFacture(@PathVariable Long id) {
        log.debug("REST request to get Facture : {}", id);
        FactureDTO factureDTO = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(factureDTO));
    }

    /**
     * DELETE  /factures/:id : delete the "id" facture.
     *
     * @param id the id of the factureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/factures/{id}")
    @Timed
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        log.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/factures?query=:query : search for the facture corresponding
     * to the query.
     *
     * @param query the query of the facture search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/factures")
    @Timed
    public ResponseEntity<List<FactureDTO>> searchFactures(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Factures for query {}", query);
        Page<FactureDTO> page = factureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/factures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

     /**
     * GET  /facture/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/last/factures/{id}/{id2}")
    @Timed
    public Long getLasteId(@PathVariable Long id,@PathVariable Long id2) {
         return factureService.lastId(id,id2);
    }

     /**
     * GET  /facture/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/facChanTrav/factures/{id}/{id2}")
    @Timed
    public ResponseEntity<Facture> getfactureChantierTravaux(@PathVariable Long id,@PathVariable Long id2) {
        Facture facture = factureService.factureChantierTravaux(id,id2);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(facture));
    }

    /**
     * GET  /facture/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/facEntreprise/factures/{id}")
    @Timed
    public ResponseEntity<List<Facture>> getfactureEntreprise(@PathVariable Long id) {
        List<Facture> factures = factureService.factureEntreprise(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(factures));
    }

     /**
     * GET  /facture/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/factureValide/factures/{id}")
    @Timed
    public ResponseEntity<List<Facture>> getfactureValide(@PathVariable Long id) {
        List<Facture> factures = factureService.factureValide(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(factures));
    }

     /**
     * GET  /facture/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/factureNonValide/factures/{id}")
    @Timed
    public ResponseEntity<List<Facture>> getfactureNonValide(@PathVariable Long id) {
        List<Facture> factures = factureService.factureNonValide(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(factures));
    }

}
