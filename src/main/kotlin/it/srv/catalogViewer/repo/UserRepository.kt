package it.srv.catalogViewer.repo

import it.srv.catalogViewer.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int>{
    fun findByEmail(email: String?): User?

    fun findByUsername(username: String?): User?
}