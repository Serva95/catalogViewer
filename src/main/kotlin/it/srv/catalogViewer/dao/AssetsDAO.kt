package it.srv.catalogViewer.dao

import it.srv.catalogViewer.model.Asset
import it.srv.catalogViewer.repo.AssetsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AssetsDAO {
    @Autowired
    private lateinit var repo: AssetsRepository

    fun save(asset: Asset): Asset? { return repo.save(asset) }

    fun getById(id: Int): Asset? { return repo.findById(id).orElse(null) }

    fun getAll(pageN: Int, order: Int): Iterable<Asset>? {
        val ordering: String = when (order) {
            1 -> "DestIp4"
            2 -> "SrcMac"
            3 -> "DestMac"
            4 -> "SrcIp6"
            5 -> "DestIp6"
            else -> "SrcIp4"
        }
        val paging = PageRequest.of(pageN, 30, Sort.by(ordering).ascending())
        return repo.findAll(paging)
    }

    fun getAllInDB(): Iterable<Asset>? { return repo.findAll() }

    fun getBySrcIP4(ip4: String, pageN: Int): Iterable<Asset>? {
        val paging = PageRequest.of(pageN, 30, Sort.by("DestIp4").ascending())
        return  repo.findBySrcIp4(ip4, paging)
    }

    fun getByDestIP4(ip4: String, pageN: Int): Iterable<Asset>? {
        val paging = PageRequest.of(pageN, 30, Sort.by("SrcIp4").ascending())
        return  repo.findByDestIp4(ip4, paging)
    }

    fun getBySrcMAC(mac: String, pageN: Int): Iterable<Asset>? {
        val paging = PageRequest.of(pageN, 30, Sort.by("DestMac").ascending())
        return repo.findBySrcMac(mac, paging)
    }

    fun getByDestMAC(mac: String, pageN: Int): Iterable<Asset>? {
        val paging = PageRequest.of(pageN, 30, Sort.by("SrcMac").ascending())
        return repo.findByDestMac(mac, paging)
    }

    fun getBySrcPort(port: Int, pageN: Int): Iterable<Asset>? {
        val paging = PageRequest.of(pageN, 30, Sort.by("DestPort").ascending())
        return repo.findBySrcPort(port, paging)
    }

    fun getByDestPort(port: Int, pageN: Int): Iterable<Asset>? {
        val paging = PageRequest.of(pageN, 30, Sort.by("SrcPort").ascending())
        return repo.findByDestPort(port, paging)
    }

    fun getByProto(proto: String, pageN: Int): Iterable<Asset>? {
        val paging = PageRequest.of(pageN, 30)
        return repo.findByProto(proto, paging)
    }

    fun getComunicationsBetweenIp4(src: String, dest: String, pageN: Int): Iterable<Asset>? {
        val paging = PageRequest.of(pageN, 30)
        return repo.findAllBySrcIp4AndDestIp4(src, dest, paging)
    }

    fun getComunicationsBetweenMac(src: String, dest:String, pageN: Int): Iterable<Asset>? {
        val paging = PageRequest.of(pageN, 30)
        return repo.findAllBySrcMacAndDestMac(src, dest, paging)
    }

    fun getDestIp4ForSrcIp4(srcIp4: String): ArrayList<String>? { return repo.findDistinctSrcIp4ByDestIp4(srcIp4)}

    fun getGroupedBySrcIp4(): Iterable<Asset>? { return repo.findAllGroupBySrcIp4() }

    fun getGroupedByDestIp4(): Iterable<Asset>? { return repo.findAllGroupByDestIp4() }

    fun getGroupedBySrcMac(): Iterable<Asset>? { return repo.findAllGroupBySrcMac() }

    fun getGroupedByDestMac(): Iterable<Asset>? { return repo.findAllGroupByDestMac() }

    fun getGroupedByProto(): Iterable<Asset>? { return repo.findAllGroupByProto() }

    fun getMaxIdValue(): Int { return repo.getMaxIdValue()}
}