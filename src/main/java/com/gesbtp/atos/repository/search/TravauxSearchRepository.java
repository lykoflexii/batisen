package com.gesbtp.atos.repository.search;

import com.gesbtp.atos.domain.Travaux;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Travaux entity.
 */
public interface TravauxSearchRepository extends ElasticsearchRepository<Travaux, Long> {
}
