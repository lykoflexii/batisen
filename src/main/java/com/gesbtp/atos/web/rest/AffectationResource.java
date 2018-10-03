package com.gesbtp.atos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gesbtp.atos.domain.Affectation;
import com.gesbtp.atos.service.AffectationService;
import com.gesbtp.atos.web.rest.errors.BadRequestAlertException;
import com.gesbtp.atos.web.rest.util.HeaderUtil;
import com.gesbtp.atos.web.rest.util.PaginationUtil;
import com.gesbtp.atos.service.dto.AffectationDTO;
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
import java.util.Hashtable;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Affectation.
 */
@RestController
@RequestMapping("/api")
public class AffectationResource {

    private final Logger log = LoggerFactory.getLogger(AffectationResource.class);

    private static final String ENTITY_NAME = "affectation";

    private final AffectationService affectationService;

    public AffectationResource(AffectationService affectationService) {
        this.affectationService = affectationService;
    }

    /**
     * POST  /affectations : Create a new affectation.
     *
     * @param affectationDTO the affectationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new affectationDTO, or with status 400 (Bad Request) if the affectation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/affectations")
    @Timed
    public ResponseEntity<AffectationDTO> createAffectation(@Valid @RequestBody AffectationDTO affectationDTO) throws URISyntaxException {
        log.debug("REST request to save Affectation : {}", affectationDTO);
        if (affectationDTO.getId() != null) {
            throw new BadRequestAlertException("A new affectation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AffectationDTO result = affectationService.save(affectationDTO);
        return ResponseEntity.created(new URI("/api/affectations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /affectations : Updates an existing affectation.
     *
     * @param affectationDTO the affectationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated affectationDTO,
     * or with status 400 (Bad Request) if the affectationDTO is not valid,
     * or with status 500 (Internal Server Error) if the affectationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/affectations")
    @Timed
    public ResponseEntity<AffectationDTO> updateAffectation(@Valid @RequestBody AffectationDTO affectationDTO) throws URISyntaxException {
        log.debug("REST request to update Affectation : {}", affectationDTO);
        if (affectationDTO.getId() == null) {
            return createAffectation(affectationDTO);
        }
        AffectationDTO result = affectationService.save(affectationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, affectationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /affectations : get all the affectations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of affectations in body
     */
    @GetMapping("/affectations")
    @Timed
    public ResponseEntity<List<AffectationDTO>> getAllAffectations(Pageable pageable) {
        log.debug("REST request to get a page of Affectations");
        Page<AffectationDTO> page = affectationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/affectations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /affectations/:id : get the "id" affectation.
     *
     * @param id the id of the affectationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the affectationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/affectations/{id}")
    @Timed
    public ResponseEntity<AffectationDTO> getAffectation(@PathVariable Long id) {
        log.debug("REST request to get Affectation : {}", id);
        AffectationDTO affectationDTO = affectationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(affectationDTO));
    }

    /**
     * DELETE  /affectations/:id : delete the "id" affectation.
     *
     * @param id the id of the affectationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/affectations/{id}")
    @Timed
    public ResponseEntity<Void> deleteAffectation(@PathVariable Long id) {
        log.debug("REST request to delete Affectation : {}", id);
        affectationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/affectations?query=:query : search for the affectation corresponding
     * to the query.
     *
     * @param query the query of the affectation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/affectations")
    @Timed
    public ResponseEntity<List<AffectationDTO>> searchAffectations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Affectations for query {}", query);
        Page<AffectationDTO> page = affectationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/affectations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /affectationss/:id : get the "id" .
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the AffectationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/affectationss/{id}")
    @Timed
    public List<Affectation> getAffectationByChantier(@PathVariable Long id) {
        log.debug("REST request to get affectation : {}", id);
        return affectationService.findAllChantier(id);

    }

    /**
     * GET  /affectationsss/:id : get the "id" .
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the AffectationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/affectationsss/{id}")
    @Timed
    public List<Affectation> getAffectationByTravaux(@PathVariable Long id) {
        log.debug("REST request to get affectation : {}", id);
        return affectationService.findAllTravaux(id);

    }

    
    /**
     * GET  /affectationsss/:id : get the "id" .
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the AffectationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/etatTravaux/affectations/{id}")
    @Timed
    public Hashtable<String, Integer> travauxTerminer(@PathVariable Long id) {
       Hashtable<String,Integer> data = new Hashtable<>();
       data.put("terminer", affectationService.travauxTerminer(id));
       data.put("enCours", affectationService.travauxEncours(id));
       data.put("enRetard", affectationService.travauxEnretard(id));
        log.debug("REST request to get affectation : {}", id);
        return data;

    }

    /**
     * GET  /affectationsss/:id : get the "id" .
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the AffectationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/dureeTache/affectations/{id}/{id2}")
    @Timed
    public  Integer dureeTache(@PathVariable Long id, @PathVariable Long id2) {
        return  affectationService.getDureeTache(id, id2);

    }

    /**
     * GET  /affectationsss/:id : get the "id" .
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the AffectationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coutTache/affectations/{id}/{id2}")
    @Timed
    public  Double coutTache(@PathVariable Long id, @PathVariable Long id2) {
       Affectation aff = affectationService.findTache(id, id2);
        return  affectationService.coutTache(aff);
    }

    /**
     * GET  /affectationsss/:id : get the "id" .
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the AffectationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coutMainDoeuvreChantier/affectations/{id}")
    @Timed
    public  Double coutTache(@PathVariable Long id) {
        List<Affectation> affectations = affectationService.findAllChantier(id);
        Double cout = 0.0;
        for(Affectation aff: affectations) {
            cout += affectationService.coutTache(aff);
        }
        return cout;
    }

     /**
     * GET  /affectationsss/:id : get the "id" .
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the AffectationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coutMainDoeuvreChantierPartache/affectations/{id}")
    @Timed
    public   Hashtable<String,Double> coutChantierParTache(@PathVariable Long id) {
        List<Affectation> affectations = affectationService.findAllChantier(id);
        Hashtable<String,Double> data = new Hashtable<>();
        // Double cout = 0.0;
        for(Affectation aff: affectations) {
            data.put(aff.getTravaux().getNomTrav(), affectationService.coutTache(aff));
            // cout += affectationService.coutTache(aff);
        }
        return data;
    }

    /**
     * GET  /affectationsss/:id : get the "id" .
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and with body the AffectationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coutChantier/affectations/{id}")
    @Timed
    public Hashtable<String,Double> coutChantier(@PathVariable Long id) {
        List<Affectation> affectations = affectationService.findAllChantier(id);
        Hashtable<String,Double> data = new Hashtable<>();
        Double coutMain =0.0, coutFact = 0.0;
        // Double cout = 0.0;
        for(Affectation aff: affectations) {
           // data.put(aff.getTravaux().getNomTrav(), affectationService.coutTache(aff));
           coutMain += affectationService.coutTacheParChatierMain(aff);
           coutFact += affectationService.coutTacheParChantierFac(aff);
        }
        data.put("cout main doeuvre",coutMain);
        data.put("cout facture", coutFact);
        return data;
    }

    

}
