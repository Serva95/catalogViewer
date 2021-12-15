package it.srv.catalogViewer.model

import lombok.NoArgsConstructor
import javax.persistence.*

@Table(
    name = "authorities", uniqueConstraints = [
        UniqueConstraint(name = "authorities_id_uindex", columnNames = ["id"])
    ]
)
@Entity
class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Column(name = "username", nullable = false)
    var username: String? = null

    @Column(name = "authority", nullable = false, length = 16)
    var authority: String? = null

    constructor(){}

    constructor(username: String?, authority: String?) {
        this.username = username
        this.authority = authority
    }
}