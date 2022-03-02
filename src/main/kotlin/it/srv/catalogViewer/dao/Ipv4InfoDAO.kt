package it.srv.catalogViewer.dao

import it.srv.catalogViewer.model.Ipv4Info
import it.srv.catalogViewer.repo.Ipv4InfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class Ipv4InfoDAO {
    @Autowired
    private lateinit var repo: Ipv4InfoRepository

    fun save(ipv4Info: Ipv4Info): Ipv4Info? { return repo.save(ipv4Info) }

    fun getByIp(ip: String): Ipv4Info? { return repo.findById(ip).orElse(null) }
}