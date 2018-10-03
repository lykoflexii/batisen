package com.gesbtp.atos.repository;

    import java.util.List;

    import com.gesbtp.atos.domain.Devis;

    import org.springframework.stereotype.Repository;

    import org.springframework.data.jpa.repository.*;
    import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Devis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DevisRepository extends JpaRepository<Devis, Long> {

    @Query(value = "select max(id) as dernierId from Devis")
    Long dernierId();

    @Query(value = " select devis from Devis devis where devis.id >=(select max(id) as dernierId from Devis)")
    Devis dernierDevis();

    @Query(value = "select devis from Devis devis where devis.client.id=:id")
    List<Devis> DevisByClientId(@Param("id") Long id);

    @Query(value = "select devis from Devis devis where devis.chantier.id=:id")
    Devis DevisByChantierId(@Param("id") Long id);
    
    @Query(value = "select devis from Devis devis where devis.client.id in (select client from Client client where client.entreprise.id=:id)")
    List<Devis> DevisEntreprise(@Param("id") Long id);
   
    @Query(value = "select devis from Devis devis where devis.client.id in (select client from Client client where client.entreprise.id=:id) order by id desc ")
    List<Devis> cinqDerniersDevis(@Param("id") Long id);
}
