package org.bdd.reporting.repository

import org.bdd.reporting.data.CommonFeature
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Repository
interface FeatureRepository : CrudRepository<CommonFeature, String> {
}