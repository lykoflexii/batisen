package com.gesbtp.atos.repository.search;

import com.gesbtp.atos.domain.LigneFacture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LigneFacture entity.
 */
public interface LigneFactureSearchRepository extends ElasticsearchRepository<LigneFacture, Long> {
}
