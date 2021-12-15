package it.srv.catalogViewer.repo

import it.srv.catalogViewer.model.Asset
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.LocalDateTime

interface AssetsRepository: PagingAndSortingRepository<Asset, Int> {

    fun findBySrcIp4(ip4: String, pageable: Pageable): Page<Asset>?

    fun findByDestIp4(ip4: String, pageable: Pageable): Page<Asset>?

    fun findBySrcMac(mac: String, pageable: Pageable): Page<Asset>?

    fun findByDestMac(mac: String, pageable: Pageable): Page<Asset>?

    fun findBySrcPort(port: Int, pageable: Pageable): Page<Asset>?

    fun findByDestPort(port: Int, pageable: Pageable): Page<Asset>?

    fun findByProto(proto: String, pageable: Pageable): Page<Asset>?

    fun findByFirstSeenIsAfter(firstSeen: LocalDateTime): Iterable<Asset>?

    fun findAllBySrcIp4AndDestIp4(srcIp4: String, destIp4: String, pageable: Pageable): Page<Asset>?

    fun findAllBySrcMacAndDestMac(srcMac: String, destMac: String, pageable: Pageable): Page<Asset>?

    @Query("select a from Asset a group by a.srcIp4 order by a.srcIp4")
    fun findAllGroupBySrcIp4(): Iterable<Asset>?

    @Query("select a from Asset a group by a.destIp4 order by a.destIp4")
    fun findAllGroupByDestIp4(): Iterable<Asset>?

    @Query("select a from Asset a group by a.srcMac order by a.srcMac")
    fun findAllGroupBySrcMac(): Iterable<Asset>?

    @Query("select a from Asset a group by a.destMac order by a.destMac")
    fun findAllGroupByDestMac(): Iterable<Asset>?

    @Query("select a from Asset a group by a.proto order by a.proto")
    fun findAllGroupByProto(): Iterable<Asset>?

    @Query("select max(a.id) from Asset a")
    fun getMaxIdValue(): Int
}