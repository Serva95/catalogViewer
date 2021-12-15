package it.srv.catalogViewer.model

import javax.persistence.*

@Table(
    name = "users", indexes = [
        Index(name = "users_username_uindex", columnList = "username", unique = true),
        Index(name = "users_email_uindex", columnList = "email", unique = true)
    ]
)
@Entity
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "email", nullable = false, length = 32)
    open var email: String? = null

    @Column(name = "username", nullable = false, length = 16)
    open var username: String? = null

    @Column(name = "password", nullable = false)
    open var password: String? = null

    @Column(name = "enabled", nullable = false)
    open var enabled: Boolean? = false
}