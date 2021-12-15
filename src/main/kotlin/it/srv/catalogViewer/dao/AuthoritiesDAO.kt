package it.srv.catalogViewer.dao

import it.srv.catalogViewer.exception.InvalidAuthorityException
import it.srv.catalogViewer.model.Authority
import it.srv.catalogViewer.repo.AuthoritiesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthoritiesDAO {
    @Autowired
    private lateinit var repo: AuthoritiesRepository

    @Throws(InvalidAuthorityException::class)
    fun save(auto: Authority): Authority? {
        if (!(auto.authority.equals("ROLE_ADMIN") || auto.authority.equals("ROLE_USER"))) {
            throw InvalidAuthorityException("Invalid authority - only ROLE_ADMIN or ROLE_USER are possible values")
        }
        return repo.save(auto)
    }

    fun getByUsername(uname: String?): ArrayList<Authority?>? {
        return repo.findByUsername(uname)
    }

    fun delete(id: Short) {
        repo.deleteById(id)
    }
}