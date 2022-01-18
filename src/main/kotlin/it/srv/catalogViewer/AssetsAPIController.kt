package it.srv.catalogViewer

import it.srv.catalogViewer.dao.AssetsDAO
import it.srv.catalogViewer.model.Asset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/assets")
class AssetsAPIController {

    @Autowired
    lateinit var assetDAO: AssetsDAO

    @GetMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allAssetsJson(@RequestParam("p", defaultValue = "0") pageN: Int,
                      @RequestParam("o", defaultValue = "0") order: Int): Map<String, ArrayList<Asset?>?> {
        val map: HashMap<String, ArrayList<Asset?>?> = HashMap()
        val assets: Iterable<Asset>? = assetDAO.getAll(pageN, order)
        if (assets == null){
            map["asset"] = null
            return map
        }
        val list: ArrayList<Asset?> = arrayListOf()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next())
        }
        map["assets"] = list
        return map
    }

    @GetMapping("/allsrcIPv4", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allsrcIPv4Json(): Map<String, ArrayList<String?>?> {
        val map: HashMap<String, ArrayList<String?>?> = HashMap()
        val assets: Iterable<Asset>? = assetDAO.getGroupedBySrcIp4()
        if (assets == null){
            map["assetssrcipv4"] = null
            return map
        }
        val list: ArrayList<String?> = arrayListOf()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next().srcIp4)
        }
        map["assetssrcipv4"] = list
        return map
    }

    @GetMapping("/alldestIPv4", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun alldestIPv4Json(): Map<String, ArrayList<String?>?> {
        val map: HashMap<String, ArrayList<String?>?> = HashMap()
        val assets: Iterable<Asset>? = assetDAO.getGroupedByDestIp4()
        if (assets == null){
            map["assetsdestipv4"] = null
            return map
        }
        val list: ArrayList<String?> = arrayListOf()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next().destIp4)
        }
        map["assetsdestipv4"] = list
        return map
    }

    @GetMapping("/allsrcMAC", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allsrcMACJson(): Map<String, ArrayList<String?>?> {
        val map: HashMap<String, ArrayList<String?>?> = HashMap()
        val assets: Iterable<Asset>? = assetDAO.getGroupedBySrcMac()
        if (assets == null){
            map["assetssrcMAC"] = null
            return map
        }
        val list: ArrayList<String?> = arrayListOf()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next().srcMac)
        }
        map["assetssrcMAC"] = list
        return map
    }

    @GetMapping("/alldestMAC", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun alldestMACJson(): Map<String, ArrayList<String?>?> {
        val map: HashMap<String, ArrayList<String?>?> = HashMap()
        val assets: Iterable<Asset>? = assetDAO.getGroupedByDestMac()
        if (assets == null){
            map["assetsdestMAC"] = null
            return map
        }
        val list: ArrayList<String?> = arrayListOf()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next().destMac)
        }
        map["assetsdestMAC"] = list
        return map
    }

    @GetMapping("/alldestIPv4from/{ip}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allSrcAndDestIPv4Json(@PathVariable ip: String): ArrayList<String>? {
        //val list: ArrayList<String>? =
        return assetDAO.getDestIp4ForSrcIp4(ip)
    }
}