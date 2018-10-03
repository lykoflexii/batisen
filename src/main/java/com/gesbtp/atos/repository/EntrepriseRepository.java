package com.gesbtp.atos.repository;

import java.util.List;

import com.gesbtp.atos.domain.Entreprise;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Entreprise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {

    @Query(value = " select entreprise from Entreprise entreprise where entreprise.id not in (select user.entreprise.id from User user)")
    List<Entreprise> listeAttente();
}
