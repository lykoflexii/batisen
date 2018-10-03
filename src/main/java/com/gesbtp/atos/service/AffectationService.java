package com.gesbtp.atos.service;

import com.gesbtp.atos.domain.Affectation;
import com.gesbtp.atos.repository.AffectationRepository;
import com.gesbtp.atos.repository.search.AffectationSearchRepository;
import com.gesbtp.atos.service.dto.AffectationDTO;
import com.gesbtp.atos.service.mapper.AffectationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.gesbtp.atos.domain.Employe;
import com.gesbtp.atos.domain.Facture;
import com.gesbtp.atos.repository.FactureRepository;
import com.gesbtp.atos.domain.LigneFacture;
import com.gesbtp.atos.repository.LigneFactureRepository;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Affectation.
 */
@Service
@Transactional
public class AffectationService {

    private final Logger log = LoggerFactory.getLogger(AffectationService.class);

    private final AffectationRepository affectationRepository;

    private final AffectationMapper affectationMapper;

    private final AffectationSearchRepository affectationSearchRepository;

    private final FactureRepository factureRepository;

    private final LigneFactureRepository ligneFactureRepository;

    public AffectationService(AffectationRepository affectationRepository, AffectationMapper affectationMapper, AffectationSearchRepository affectationSearchRepository, FactureRepository factureRepository, LigneFactureRepository ligneFactureRepository) {
        this.affectationRepository = affectationRepository;
        this.affectationMapper = affectationMapper;
        this.affectationSearchRepository = affectationSearchRepository;
        this.factureRepository = factureRepository;
        this.ligneFactureRepository = ligneFactureRepository;
    }

    /**
     * Save a affectation.
     *
     * @param affectationDTO the entity to save
     * @return the persisted entity
     */
    public AffectationDTO save(AffectationDTO affectationDTO) {
        log.debug("Request to save Affectation : {}", affectationDTO);
        Affectation affectation = affectationMapper.toEntity(affectationDTO);
        affectation = affectationRepository.save(affectation);
        AffectationDTO result = affectationMapper.toDto(affectation);
        affectationSearchRepository.save(affectation);
        return result;
    }

    /**
     * Get all the affectations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AffectationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Affectations");
        return affectationRepository.findAll(pageable)
            .map(affectationMapper::toDto);
    }

    /**
     * Get one affectation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AffectationDTO findOne(Long id) {
        log.debug("Request to get Affectation : {}", id);
        Affectation affectation = affectationRepository.findOneWithEagerRelationships(id);
        return affectationMapper.toDto(affectation);
    }

    /**
     * Delete the affectation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Affectation : {}", id);
        affectationRepository.delete(id);
        affectationSearchRepository.delete(id);
    }

    /**
     * Search for the affectation corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AffectationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Affectations for query {}", query);
        Page<Affectation> result = affectationSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(affectationMapper::toDto);
    }

    /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Affectation> findAllChantier(Long id) {
        log.debug("Request to get all Chantiers");
        return affectationRepository.findAffectationByChantierAndId(id);
    }

    /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Affectation> findAllTravaux(Long id) {
        log.debug("Request to get all Chantiers");
        return affectationRepository.findAffectationByTravauxAndId(id);
    }

    /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Integer travauxTerminer(Long id) {
        log.debug("travauxTerminer");
        return affectationRepository.travauxTerminer(id).size();
    }

    /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Integer travauxEncours(Long id) {
        log.debug("travauxTerminer");
        return affectationRepository.travauxEncours(id).size();
    }

    /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Integer travauxEnretard(Long id) {
        log.debug("travauxTerminer");
        return affectationRepository.travauxEnretard(id).size();
    }

    /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Affectation findTache(Long id, Long id2) {
        log.debug("travauxTerminer");
        return affectationRepository.findAffectationByChantierAndTravaux(id,id2);
    }

     /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Integer getDureeTache(Long id, Long id2) {
        log.debug("travauxTerminer");
        Affectation aff = affectationRepository.findAffectationByChantierAndTravaux(id,id2);
        Date debut = new Date();
        Date fin = new Date();
       int m = aff.getDateDebut().getMonthValue();
       int a = aff.getDateDebut().getYear();
       int j = aff.getDateDebut().getDayOfMonth();
       int m1 = aff.getDateFin().getMonthValue();
       int a1 = aff.getDateFin().getYear();
       int j1 = aff.getDateFin().getDayOfMonth();
       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		 try {
			debut = formatter.parse(""+ j +"/"+ m +"/"+ a);
            fin = formatter.parse(""+ j1+ "/"+ m1+ "/"+ a1); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
   
    return this.getWorkingNumber(debut,fin);
    }

    public int getWorkingNumber(Date startDate,Date endDate) {
			
		int nbrJour=0;
		Calendar calendar = Calendar.getInstance();
        Date date;	 
			 date= startDate;
			 calendar.setTime(endDate);
			 // traitement de la date finale
			 if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
				 calendar.add(Calendar.DAY_OF_MONTH, -1);
				 endDate=calendar.getTime(); 
			 }else if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
				 calendar.add(Calendar.DAY_OF_MONTH, -2);
				 endDate=calendar.getTime(); 
			 }
			 
			 
			 calendar.setTime(date);
			 for(;date.before(endDate)  ;) {
				 if(calendar.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY )
			        {	date=calendar.getTime(); 
			        	++nbrJour;
			        	
			        }
			        calendar.add(Calendar.DAY_OF_MONTH, 1);
			 }
			 
			return nbrJour;		
    }
    
    /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Double coutTache(Affectation aff) {
        log.debug("Request to get all Chantiers");
        Double cout = 0.0;
        List <Facture> factures= this.factureRepository.factureChantierTravauxValider(aff.getChantier().getId(), aff.getTravaux().getId());
        for(Facture fac : factures)
        {
            List<LigneFacture> ligeneDeFacture = this.ligneFactureRepository.ligneFactureByFactureId(fac.getId());
            for(LigneFacture ligne: ligeneDeFacture)
            {
                cout += ligne.getPrixUnitaire() * ligne.getQuantiteRetenue();
            }
        }
       // Double cout = 0.0;
        int duree = getDureeTache(aff.getChantier().getId(), aff.getTravaux().getId());
        for(Employe emp : aff.getEmployes()){
         cout += emp.getSalaire() * duree;
         }
        return cout;
    }

    /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Double coutTacheParChatierMain(Affectation aff) {
        log.debug("Request to get all Chantiers");
        Double cout = 0.0;
       // Double cout = 0.0;
        int duree = getDureeTache(aff.getChantier().getId(), aff.getTravaux().getId());
        for(Employe emp : aff.getEmployes()){
         cout += emp.getSalaire() * duree;
         }
        return cout;
    }

    /**
     * Get all the chantiers.
     *
     * @param id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Double coutTacheParChantierFac(Affectation aff) {
        log.debug("Request to get all Chantiers");
        Double cout = 0.0;
        List <Facture> factures= this.factureRepository.factureChantierTravauxValider(aff.getChantier().getId(), aff.getTravaux().getId());
        for(Facture fac : factures)
        {
            List<LigneFacture> ligeneDeFacture = this.ligneFactureRepository.ligneFactureByFactureId(fac.getId());
            for(LigneFacture ligne: ligeneDeFacture)
            {
                cout += ligne.getPrixUnitaire() * ligne.getQuantiteRetenue();
            }
        }
        return cout;
    }
}
