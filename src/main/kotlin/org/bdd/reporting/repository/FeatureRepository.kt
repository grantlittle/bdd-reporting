package org.bdd.reporting.repository

import org.bdd.reporting.data.CommonFeature
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Repository
interface FeatureRepository : ElasticsearchRepository<CommonFeature, String> {

    @Query("""
        {
          "query": {
              "bool": {
                  "must": [
                    { "match": { "name": "?0" } }
                  ]
              }
          },
          "size" : 1
        }
    """)
    fun findByName(name : String) : Set<CommonFeature>


    @Query("""
        {
          "query": {
              "bool": {
                  "must": [
                    { "match": { "tags.name": "?0" } }
                  ]
              }
          },
          "size" : 1
        }
    """)
    fun findByTag(tag : String) : Set<CommonFeature>

}