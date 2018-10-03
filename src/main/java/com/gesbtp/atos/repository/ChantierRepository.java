package com.gesbtp.atos.repository;

import com.gesbtp.atos.domain.Chantier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Chantier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChantierRepository extends JpaRepository<Chantier, Long> {

    @Query("select chantier from Chantier chantier where chantier.user.login = ?#{principal.username}")
    List<Chantier> findByUserIsCurrentUser();
    
    @Query("select chantier from Chantier chantier where chantier.user.login = ?#{principal.username} and chantier.client.id=:id")
    List<Chantier> findByUserIsCurrentUserAndClient(@Param("id") Long id);

    @Query(value = "select chantier from Chantier chantier where chantier.client.id=:id")
    List<Chantier> findByClientId(@Param("id") Long id);

    @Query(value = "select chantier from Chantier chantier where chantier.client in (select client from Client client where client.entreprise.id=:id)")
    List<Chantier> findChantierEntrprise(@Param("id") Long id);
    
    @Query(value = "select count(*) from Chantier chantier where chantier.etatChantier = 'TERMINER' and chantier.client in (select client from Client client where client.entreprise.id=:id)")
    Integer chantierTerminer(@Param("id") Long id);

    @Query(value = "select count(*) from Chantier chantier where chantier.etatChantier = 'EN_COURS' and chantier.client in (select client from Client client where client.entreprise.id=:id)")
    Integer chantierEnCours(@Param("id") Long id);

    @Query(value = "select count(*) from Chantier chantier where chantier.etatChantier = 'EN_RETARD' and chantier.client in (select client from Client client where client.entreprise.id=:id)")
    Integer chantierEnRetard(@Param("id") Long id);

    @Query(value = "select count(*) from Chantier chantier where chantier.etatChantier = 'TERMINER' and chantier.client.id=:id")
    Integer chantierTerminerParClient(@Param("id") Long id);

    @Query(value = "select count(*) from Chantier chantier where chantier.etatChantier = 'EN_COURS' and chantier.client.id=:id")
    Integer chantierEnCoursParClient(@Param("id") Long id);

    @Query(value = "select count(*) from Chantier chantier where chantier.etatChantier = 'EN_RETARD' and chantier.client.id=:id")
    Integer chantierEnRetardParClient(@Param("id") Long id);
}
