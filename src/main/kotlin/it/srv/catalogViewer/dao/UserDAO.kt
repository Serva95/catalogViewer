package it.srv.catalogViewer.dao

import it.srv.catalogViewer.model.User
import it.srv.catalogViewer.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserDAO {
    @Autowired
    private lateinit var repo: UserRepository

    fun save(user: User): User? {
        return repo.save(user)
    }

    fun getById(id: Int): User? {
        return repo.findById(id).orElse(null)
    }

    fun getAll(): Iterable<User>? {
        return repo.findAll()
    }

    fun getByMail(email: String): User? {
        return repo.findByEmail(email)
    }

    fun getByUsername(uname: String): User? {
        return repo.findByUsername(uname)
    }

    fun delete(id: Int) {
        repo.deleteById(id)
    }

    fun exists(user: User?): Boolean {
        return user != null
    }
}