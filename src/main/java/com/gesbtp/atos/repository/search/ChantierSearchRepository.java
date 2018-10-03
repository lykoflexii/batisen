package com.gesbtp.atos.repository.search;

import com.gesbtp.atos.domain.Chantier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Chantier entity.
 */
public interface ChantierSearchRepository extends ElasticsearchRepository<Chantier, Long> {
}
