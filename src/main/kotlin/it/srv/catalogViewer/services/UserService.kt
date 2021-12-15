package it.srv.catalogViewer.services

import it.srv.catalogViewer.dao.AuthoritiesDAO
import it.srv.catalogViewer.dao.UserDAO
import it.srv.catalogViewer.dto.UserDTO
import it.srv.catalogViewer.exception.InvalidAuthorityException
import it.srv.catalogViewer.exception.ObjectAlreadyExistException
import it.srv.catalogViewer.exception.PasswordException
import it.srv.catalogViewer.exception.UsernameAlreadyExistException
import it.srv.catalogViewer.model.Authority
import it.srv.catalogViewer.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService {

    @Autowired
    fun argon2PasswordEncoder(): Argon2PasswordEncoder {
        return Argon2PasswordEncoder(16, 64, 8, 1 shl 13, 16)
    }

    @Autowired
    lateinit var userDAO: UserDAO

    @Autowired
    lateinit var authoritiesDAO: AuthoritiesDAO

    @Throws(ObjectAlreadyExistException::class, UsernameAlreadyExistException::class, PasswordException::class)
    fun registerNewUserAccount(account: UserDTO): User? {
        if (userDAO.exists(userDAO.getByMail(account.email!!))) {
            throw ObjectAlreadyExistException(
                "È già presente un utente con questa mail: " + account.email
                    .toString() + ", prova con una diversa."
            )
        }
        if (userDAO.exists(userDAO.getByUsername(account.username!!))) {
            throw UsernameAlreadyExistException(
                "È già presente un nome utente come questo: " + account.username
                    .toString() + ", prova con uno diverso."
            )
        }
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
        if (account.password != null) {
            if (!account.password.equals(account.matchingPassword) || account.password!!.length < 8) {
                throw PasswordException("Le due password sono diverse, ricontrolla e riprova.")
            } else if (!account.password!!.matches(regex)) {
                throw PasswordException("La password non rispetta le caratteristiche, ricontrolla e riprova.")
            }
        } else {
            throw PasswordException("La password non può essere vuota")
        }
        var user = User()
        user.password = argon2PasswordEncoder().encode(account.password)
        user.email = account.email
        user.username = account.username
        user = userDAO.save(user)!!
        val authorities = Authority(account.username, "ROLE_USER")
        try {
            authoritiesDAO.save(authorities)
        } catch (e: InvalidAuthorityException) {
            e.printStackTrace()
        }
        return user
    }

    @Throws(ObjectAlreadyExistException::class, PasswordException::class)
    fun updateAccountData(actualUser: User, newUser: UserDTO) {
        if (!argon2PasswordEncoder().matches(newUser.oldPassword, actualUser.password)) {
            throw PasswordException("La password attuale inserita è errata, controlla meglio e riprova")
        }
        if (actualUser.email!!.compareTo(newUser.email!!, true) != 0
            && userDAO.exists(userDAO.getByMail(newUser.email!!))
        ) {
            throw ObjectAlreadyExistException(
                "È già presente un utente con questa mail: " + newUser.email.toString() + ", prova con una diversa."
            )
        }
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
        if (newUser.password != null && newUser.password != "") {
            if (newUser.password!!.matches(regex) && newUser.password!!.compareTo(newUser.matchingPassword!!) == 0
            ) {
                actualUser.password = argon2PasswordEncoder().encode(newUser.password)
            } else if (!newUser.password!!.matches(regex)) {
                throw PasswordException("La password non rispetta i caratteri richiesti")
            } else {
                throw PasswordException("La nuova password e ripeti password non corrispondono, riprova.")
            }
        }
        actualUser.username = newUser.username
        actualUser.email = newUser.email
        userDAO.save(actualUser)
    }
}