package it.srv.catalogViewer.services

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import it.srv.catalogViewer.dao.AssetsDAO
import it.srv.catalogViewer.dao.Ipv4InfoDAO
import it.srv.catalogViewer.model.Ipv4Info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class AssetService {

    private val client = HttpClient(CIO)

    @Autowired
    lateinit var assetDAO: AssetsDAO

    @Autowired
    lateinit var ipv4InfoDAO: Ipv4InfoDAO

    //ipinfo.io/62.11.218.105?token=9a59928335d11e
    suspend fun getIpv4Info(ip: String): Ipv4Info {
        val spl = ip.split(".")
        return if (!(spl[0] == "10" || (spl[0] == "172" && spl[1].toInt() in 16..31) || (spl[0] == "192" && spl[1] == "168")
                    || spl[0] == "0" || spl[0] == "255")){
            val url = "https://ipinfo.io/${ip}/?token=9a59928335d11e"
            val search = client.request<String>(url)
            val ipv4Info = Ipv4Info(ip, search, LocalDateTime.now())
            withContext(Dispatchers.IO) { ipv4InfoDAO.save(ipv4Info) }
            ipv4Info
        } else {
            Ipv4Info(ip, "ip locale, nessuna info trovata", LocalDateTime.now())
        }
    }

    fun getAllIpv4Info(): String {
        val ipv4s = assetDAO.getGroupedBySrcIp4()
        return try {
            ipv4s?.iterator()?.forEach { t ->
                if (t.srcIp4 != null) {
                    val info = ipv4InfoDAO.getByIp(t.srcIp4!!)
                    if (info == null) {
                        runBlocking { getIpv4Info(t.srcIp4!!) }
                    }
                }
            }
            "ok"
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun hexToBinary(hex: String) = Integer.toBinaryString(hex.toInt(16))

}