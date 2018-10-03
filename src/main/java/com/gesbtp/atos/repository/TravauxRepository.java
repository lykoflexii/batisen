package com.gesbtp.atos.repository;

import com.gesbtp.atos.domain.Travaux;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Travaux entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TravauxRepository extends JpaRepository<Travaux, Long> {

}
