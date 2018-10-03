package com.gesbtp.atos.repository;

import java.util.List;

import com.gesbtp.atos.domain.LigneDevis;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LigneDevis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneDevisRepository extends JpaRepository<LigneDevis, Long> {

    @Query(value = "select ligneDevis from LigneDevis ligneDevis where ligneDevis.devis.id=:id")
    List<LigneDevis> ligneDevisByDevisId(@Param("id") Long id);

}
