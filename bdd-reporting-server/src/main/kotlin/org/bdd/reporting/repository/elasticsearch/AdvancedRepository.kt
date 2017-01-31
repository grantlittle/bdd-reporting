package org.bdd.reporting.repository.elasticsearch

import org.bdd.reporting.data.CommonFeature
import org.elasticsearch.index.query.QueryBuilders.boolQuery
import org.elasticsearch.index.query.QueryBuilders.matchQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Service
class AdvancedRepository(val ops : ElasticsearchOperations) {

    fun tagSearch(tag : String) : Set<CommonFeature> {
        val builder = NativeSearchQueryBuilder()
        val query = builder.withQuery(boolQuery().must(matchQuery("tags.name", tag))).build()
        return ops.queryForList(query, CommonFeature::class.java).toSet()
    }

}