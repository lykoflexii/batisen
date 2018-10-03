package com.gesbtp.atos.repository.search;

import com.gesbtp.atos.domain.Affectation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Affectation entity.
 */
public interface AffectationSearchRepository extends ElasticsearchRepository<Affectation, Long> {
}
