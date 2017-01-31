package org.bdd.reporting.repository.jpa

import org.bdd.reporting.data.DbEvent
import org.bdd.reporting.data.DbEventKey
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 */
@Repository
interface EventRepository : CrudRepository<DbEvent, DbEventKey>