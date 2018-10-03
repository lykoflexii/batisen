package com.gesbtp.atos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gesbtp.atos.domain.Chantier;
import com.gesbtp.atos.security.SecurityUtils;
import com.gesbtp.atos.service.ChantierService;
import com.gesbtp.atos.web.rest.errors.BadRequestAlertException;
import com.gesbtp.atos.web.rest.util.HeaderUtil;
import com.gesbtp.atos.web.rest.util.PaginationUtil;
import com.gesbtp.atos.service.dto.ChantierDTO;
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
 * REST controller for managing Chantier.
 */
@RestController
@RequestMapping("/api")
public class ChantierResource {

    private final Logger log = LoggerFactory.getLogger(ChantierResource.class);

    private static final String ENTITY_NAME = "chantier";

    private final ChantierService chantierService;

    public ChantierResource(ChantierService chantierService) {
        this.chantierService = chantierService;
    }

    /**
     * POST  /chantiers : Create a new chantier.
     *
     * @param chantierDTO the chantierDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chantierDTO, or with status 400 (Bad Request) if the chantier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chantiers")
    @Timed
    public ResponseEntity<ChantierDTO> createChantier(@Valid @RequestBody ChantierDTO chantierDTO) throws URISyntaxException {
        log.debug("REST request to save Chantier : {}", chantierDTO);
        if (chantierDTO.getId() != null) {
            throw new BadRequestAlertException("A new chantier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChantierDTO result = chantierService.save(chantierDTO);
        return ResponseEntity.created(new URI("/api/chantiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chantiers : Updates an existing chantier.
     *
     * @param chantierDTO the chantierDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chantierDTO,
     * or with status 400 (Bad Request) if the chantierDTO is not valid,
     * or with status 500 (Internal Server Error) if the chantierDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chantiers")
    @Timed
    public ResponseEntity<ChantierDTO> updateChantier(@Valid @RequestBody ChantierDTO chantierDTO) throws URISyntaxException {
        log.debug("REST request to update Chantier : {}", chantierDTO);
        if (chantierDTO.getId() == null) {
            return createChantier(chantierDTO);
        }
        ChantierDTO result = chantierService.save(chantierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chantierDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chantiers : get all the chantiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of chantiers in body
     */
    @GetMapping("/chantiers")
    @Timed
    public ResponseEntity<List<ChantierDTO>> getAllChantiers(Pageable pageable) {
        log.debug("REST request to get a page of Chantiers");
        Page<ChantierDTO> page = chantierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /chantiers/:id : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chantiers/{id}")
    @Timed
    public ResponseEntity<ChantierDTO> getChantier(@PathVariable Long id) {
        log.debug("REST request to get Chantier : {}", id);
        ChantierDTO chantierDTO = chantierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chantierDTO));
    }

    /**
     * DELETE  /chantiers/:id : delete the "id" chantier.
     *
     * @param id the id of the chantierDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chantiers/{id}")
    @Timed
    public ResponseEntity<Void> deleteChantier(@PathVariable Long id) {
        log.debug("REST request to delete Chantier : {}", id);
        chantierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/chantiers?query=:query : search for the chantier corresponding
     * to the query.
     *
     * @param query the query of the chantier search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/chantiers")
    @Timed
    public ResponseEntity<List<ChantierDTO>> searchChantiers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Chantiers for query {}", query);
        Page<ChantierDTO> page = chantierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/chantiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /chantiers/:id : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chantierss/{id}")
    @Timed
    public List<Chantier> getChantiers(@PathVariable Long id) {
        log.debug("REST request to get Chantier : {}", id);
        return chantierService.findAll(id);

    }

    /**
     * GET  /filter/chantiers/:id : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/filter/chantiers/{id}")
    @Timed
    public ResponseEntity<List<Chantier>> findChantierEntrprise(@PathVariable Long id) {
        log.debug("REST request to get Chantier : {}", id);
        List<Chantier> chantiers = chantierService.findChantierEntrprise(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chantiers));
    }

    /**
     * GET  /parUser/chantiers/: : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/parUser/chantiers")
    @Timed
    public ResponseEntity<List<Chantier>> findByUserIsCurrentUser() {
        log.debug("REST request to get Chantier : {}");
        List<Chantier> chantiers = chantierService.findByUserIsCurrentUser();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chantiers));
    }

    /**
     * GET  /filter/chantiers/:id : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/parUserClient/chantiers/{id}")
    @Timed
    public ResponseEntity<List<Chantier>> findByUserIsCurrentUserAndClient(@PathVariable Long id) {
        log.debug("REST request to get Chantier : {}", id);
        List<Chantier> chantiers = chantierService.findByUserIsCurrentUserAndClient(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chantiers));
    }

     /**
     * GET  /filter/chantiers/:id : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    // @GetMapping("/terminer/chantiers/{id}")
    // @Timed
    // public Integer chantierTerminer(@PathVariable Long id) {
    //     log.debug("REST request to get Chantier : {}", id);
     
    //     return chantierService.chantierTerminer(id);
    // }

    /**
     * GET  /filter/chantiers/:id : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/etatChantier/chantiers/{id}")
    @Timed
    public Hashtable<String, Integer> etatChantier(@PathVariable Long id) {
        Hashtable<String,Integer> data = new Hashtable<>();
        data.put("terminer", chantierService.chantierTerminer(id));
        data.put("enCours", chantierService.chantierEnCours(id));
        data.put("enRetard", chantierService.chantierEnRetard(id));
         log.debug("REST request to get affectation : {}", id);
         return data;
 
     }

     /**
     * GET  /filter/chantiers/:id : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    @GetMapping("/etatChantierParClient/chantiers/{id}")
    @Timed
    public Hashtable<String, Integer> etatChantierParClient(@PathVariable Long id) {
        Hashtable<String,Integer> data = new Hashtable<>();
        data.put("terminer", chantierService.chantierTerminerParClient(id));
        data.put("enCours", chantierService.chantierEnCoursParClient(id));
        data.put("enRetard", chantierService.chantierEnRetardParClient(id));
         log.debug("REST request to get affectation : {}", id);
         return data;
 
     }

     /**
     * GET  /filter/chantiers/:id : get the "id" chantier.
     *
     * @param id the id of the chantierDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chantierDTO, or with status 404 (Not Found)
     */
    // @GetMapping("/enRetard/chantiers/{id}")
    // @Timed
    // public Integer chantierEnRetard(@PathVariable Long id) {
    //     log.debug("REST request to get Chantier : {}", id);
     
    //     return chantierService.chantierEnRetard(id);
    // }
}
