package com.gesbtp.atos.repository.search;

import com.gesbtp.atos.domain.Devis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Devis entity.
 */
public interface DevisSearchRepository extends ElasticsearchRepository<Devis, Long> {
}
