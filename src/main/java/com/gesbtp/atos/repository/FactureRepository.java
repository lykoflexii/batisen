package com.gesbtp.atos.repository;

import java.util.List;

import com.gesbtp.atos.domain.Facture;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Facture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    @Query(value = "select max(id) as dernierId from Facture facture where facture.chantier.id=:id and facture.travaux.id=:id2")
    Long dernierId(@Param("id") Long id,@Param("id2") Long id2);

    
    @Query(value = "select facture from Facture facture where facture.chantier.id=:id and facture.travaux.id=:id2")
    Facture factureChantierTravaux(@Param("id") Long id,@Param("id2") Long id2);

    @Query(value = "select facture from Facture facture where facture.entreprise.id=:id")
    List<Facture> factureEntreprise(@Param("id") Long id);

    @Query(value = "select facture from Facture facture where facture.valider=true and facture.entreprise.id=:id")
    List<Facture> factureValide(@Param("id") Long id);

    @Query(value = "select facture from Facture facture where facture.valider=false and facture.entreprise.id=:id")
    List<Facture> factureNonValide(@Param("id") Long id);

    @Query(value = "select facture from Facture facture where facture.chantier.id=:id and facture.travaux.id=:id2 and facture.valider=true")
    List <Facture> factureChantierTravauxValider(@Param("id") Long id,@Param("id2") Long id2);

}
