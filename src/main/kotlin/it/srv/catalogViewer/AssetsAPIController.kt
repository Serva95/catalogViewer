package it.srv.catalogViewer

import it.srv.catalogViewer.dao.AssetsDAO
import it.srv.catalogViewer.model.Asset
import it.srv.catalogViewer.services.AssetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/assets")
class AssetsAPIController {

    @Autowired
    lateinit var assetDAO: AssetsDAO

    @Autowired
    lateinit var assetService: AssetService

    /**
     * get all assets in jason format
     */
    @GetMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allAssetsJson(@RequestParam("p", defaultValue = "0") pageN: Int,
                      @RequestParam("o", defaultValue = "0") order: Int): Map<String, ArrayList<Asset?>?> {
        val map: HashMap<String, ArrayList<Asset?>?> = HashMap()
        val assets = assetDAO.getAll(pageN, order)
        if (assets == null){
            map["asset"] = null
            return map
        }
        val list = arrayListOf<Asset?>()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next())
        }
        map["assets"] = list
        return map
    }

    /**
     * all assets grouped by src ipv4 in json format
     */
    @GetMapping("/allsrcIPv4", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allsrcIPv4Json(): Map<String, ArrayList<String?>?> {
        val map: HashMap<String, ArrayList<String?>?> = HashMap()
        val assets = assetDAO.getGroupedBySrcIp4()
        if (assets == null){
            map["assetssrcipv4"] = null
            return map
        }
        val list = arrayListOf<String?>()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next().srcIp4)
        }
        map["assetssrcipv4"] = list
        return map
    }

    /**
     * all assets grouped by dest ipv4 in json format
     */
    @GetMapping("/alldestIPv4", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun alldestIPv4Json(): Map<String, ArrayList<String?>?> {
        val map: HashMap<String, ArrayList<String?>?> = HashMap()
        val assets = assetDAO.getGroupedByDestIp4()
        if (assets == null){
            map["assetsdestipv4"] = null
            return map
        }
        val list = arrayListOf<String?>()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next().destIp4)
        }
        map["assetsdestipv4"] = list
        return map
    }

    /**
     * all assets grouped by src mac in json format
     */
    @GetMapping("/allsrcMAC", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allsrcMACJson(): Map<String, ArrayList<String?>?> {
        val map: HashMap<String, ArrayList<String?>?> = HashMap()
        val assets = assetDAO.getGroupedBySrcMac()
        if (assets == null){
            map["assetssrcMAC"] = null
            return map
        }
        val list = arrayListOf<String?>()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next().srcMac)
        }
        map["assetssrcMAC"] = list
        return map
    }

    /**
     * all assets grouped by dest mac in json format
     */
    @GetMapping("/alldestMAC", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun alldestMACJson(): Map<String, ArrayList<String?>?> {
        val map: HashMap<String, ArrayList<String?>?> = HashMap()
        val assets = assetDAO.getGroupedByDestMac()
        if (assets == null){
            map["assetsdestMAC"] = null
            return map
        }
        val list = arrayListOf<String?>()
        val iter = assets.iterator()
        while (iter.hasNext()){
            list.add(iter.next().destMac)
        }
        map["assetsdestMAC"] = list
        return map
    }

    /* da fare mapping
|   N   |   C   |   E   |   U   |   A   |   P   |   R   |   S   |   F   |    Hex   |    Binary   |
|  256  |  128  |   64  |   32  |   16  |   8   |   4   |   2   |   1   |          |             |
|------------------------------------------------------------------------------------------------|
|   0   |   0   |   1   |   0   |   1   |   0   |   0   |   1   |   0   |   0x52   |   1010010   |
|       |   1   |   1   |   0   |   1   |   1   |   1   |   1   |   1   |   0xdf   |   11011111  |
     */

    /**
     * all assets flags in json format - WIP
     */
    @GetMapping("/getflags", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllFlagsJson(): Map<String, ArrayList<String>?> {
        val map = HashMap<String, ArrayList<String>?>()
        val flags = assetDAO.getAllFlags()
        if (flags == null){
            map["flags"] = null
            return map
        } else
            map["flags"] = flags
        return map
    }

    /**
     * all assets grouped by flag in json format - WIP
     */
    @GetMapping("/getflags/{flag}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFlagsJson(@PathVariable flag: String): Map<String, ArrayList<String>?> {
        val map = HashMap<String, ArrayList<String>?>()
        val assets = assetDAO.getByFlag(flag)
        if (assets == null){
            map["flags"] = null
            return map
        } else
            map["flags"] = assets
        return map
    }

    /**
     * all ipv4 called by a src ipv4 in json format
     */
    @GetMapping("/alldestIPv4from/{ip}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allSrcAndDestIPv4Json(@PathVariable ip: String): ArrayList<String>? {
        return assetDAO.getDestIp4ForSrcIp4(ip)
    }

    /**
     * user message for all ipv4 info search
     */
    @GetMapping("/getSrcIPv4Infos")
    fun getSrcIPv4Infos(): String {
        val result = assetService.getAllIpv4Info()
        return if (result == "ok")
            "All ip info retrieved and saved with success"
        else
            "There was a problem with your request: $result"

    }
}