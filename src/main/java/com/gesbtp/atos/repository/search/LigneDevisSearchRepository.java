package com.gesbtp.atos.repository.search;

import com.gesbtp.atos.domain.LigneDevis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LigneDevis entity.
 */
public interface LigneDevisSearchRepository extends ElasticsearchRepository<LigneDevis, Long> {
}
