package com.gesbtp.atos.repository.search;

import com.gesbtp.atos.domain.Entreprise;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Entreprise entity.
 */
public interface EntrepriseSearchRepository extends ElasticsearchRepository<Entreprise, Long> {
}
