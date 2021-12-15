package it.srv.catalogViewer.repo

import it.srv.catalogViewer.model.Authority
import org.springframework.data.repository.CrudRepository

interface AuthoritiesRepository : CrudRepository<Authority, Short> {
    fun findByUsername(username: String?): ArrayList<Authority?>?
}