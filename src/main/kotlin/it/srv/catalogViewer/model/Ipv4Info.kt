package it.srv.catalogViewer.model

import org.json.JSONObject
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "ipv4_info", schema = "ip")
class Ipv4Info {
    constructor(ip: String, info: String, date: LocalDateTime) : this() {
        this.ip = ip
        this.info = info
        this.date = date
    }

    constructor()

    @Id
    @Column(name = "ip", nullable = false)
    var ip: String? = null

    @Column(name = "info", nullable = false)
    var info: String? = null

    @Column(name = "date", nullable = false)
    var date: LocalDateTime? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "ip = $ip " +
                "info = $info " +
                "date = $date " +
                ")"

    fun prettyJson(): String {
        return try {
            val jsonObject = JSONObject(info)
            jsonObject.toString(3)
        } catch (e: Exception) {
            info.orEmpty()
        }
    }
}

