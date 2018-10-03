package com.gesbtp.atos.repository;

import java.util.List;

import com.gesbtp.atos.domain.LigneFacture;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the LigneFacture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneFactureRepository extends JpaRepository<LigneFacture, Long> {

    @Query(value = "select ligneFacture from LigneFacture ligneFacture where ligneFacture.facture.id=:id")
    List<LigneFacture> ligneFactureByFactureId(@Param("id") Long id);
}
