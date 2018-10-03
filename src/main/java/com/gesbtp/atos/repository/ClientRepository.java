package com.gesbtp.atos.repository;

import com.gesbtp.atos.domain.Client;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Client entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = " select client from Client client where client.entreprise.id=:id")
    List<Client> findEntrepriseClient(@Param("id") Long id);

}
