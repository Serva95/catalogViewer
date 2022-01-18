package it.srv.catalogViewer.model

import java.time.LocalDateTime
import javax.persistence.*

@Table(
    name = "communications", uniqueConstraints = [
        UniqueConstraint(name = "communications_id_uindex", columnNames = ["id"])
    ]
)
@Entity
class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Column(name = "src_ip4", length = 16)
    var srcIp4: String? = null

    @Column(name = "dest_ip4", length = 16)
    var destIp4: String? = null

    @Column(name = "src_ip6", length = 32)
    var srcIp6: String? = null

    @Column(name = "dest_ip6", length = 32)
    var destIp6: String? = null

    @Column(name = "src_mac", length = 17)
    var srcMac: String? = null

    @Column(name = "dest_mac", length = 17)
    var destMac: String? = null

    @Column(name = "src_port")
    var srcPort: Int? = null

    @Column(name = "dest_port")
    var destPort: Int? = null

    @Column(name = "proto", length = 10)
    var proto: String? = null

    @Column(name = "flags", length = 10)
    var flags: String? = null

    @Column(name = "first_seen", nullable = false)
    var firstSeen: LocalDateTime? = null

    @Column(name = "last_seen")
    var lastSeen: LocalDateTime? = null

    fun toCommaList(): String {
        return srcIp4 +","+ destIp4 +","+ srcIp6 +","+ destIp6 +","+ srcMac  +","+ destMac  +","+ srcPort.toString() +
                ","+ destPort.toString() +","+ (proto?: "nessun protocollo") + ","+ (firstSeen.toString()?: "") +
                ","+ (lastSeen.toString()?: "")
    }
}