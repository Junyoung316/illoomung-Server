package com.reserve.illoomung.domain.repository.es;

import com.reserve.illoomung.domain.entity.es.StoreDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StoreSearchRepository extends ElasticsearchRepository<StoreDocument, Long> {
}
