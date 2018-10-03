package com.gesbtp.atos.repository;

import java.util.List;

import com.gesbtp.atos.domain.Employe;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Employe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    @Query(value = " select employe from Employe employe where employe.entreprise.id=:id")
    List<Employe> findEntrepriseEmploye(@Param("id") Long id);
}
