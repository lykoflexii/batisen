package com.gesbtp.atos.repository.search;

import com.gesbtp.atos.domain.Employe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Employe entity.
 */
public interface EmployeSearchRepository extends ElasticsearchRepository<Employe, Long> {
}
