package it.srv.catalogViewer.dto

import it.srv.catalogViewer.model.User
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

class UserDTO(var username: String?, var password: String?, var matchingPassword: String?, var email: String?, var oldPassword: String?) {

    fun userDTOtoUser(): User {
        val user = User()
        user.email = (this.email)
        user.username = (this.username)
        user.password = (this.password)
        return user
    }

}