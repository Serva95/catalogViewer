package it.srv.catalogViewer

import it.srv.catalogViewer.dao.AssetsDAO
import it.srv.catalogViewer.model.Asset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView


@RestController
class AssetController {

    @Autowired
    lateinit var assetDAO: AssetsDAO

    @GetMapping("/assets/com/{type}")
    fun viewComunicationsBetween(mav: ModelAndView, @PathVariable type: String, @RequestParam("pageN", defaultValue = "0") pageN: Int,
                                  @RequestParam("s") s: String, @RequestParam("d") d: String): ModelAndView {
        val assets: Iterable<Asset>? = when(type) {
            "4" -> assetDAO.getComunicationsBetweenIp4(s, d, pageN)
            "Mac" -> assetDAO.getComunicationsBetweenMac(s, d, pageN)
            else -> null
        }
        mav.viewName = "comunicationBetween"
        mav.addObject("assets", assets)
        mav.addObject("pageN", pageN)
        return mav
    }

    @GetMapping("/assets/{param}/{id}")
    fun singlesrcIPv4(mav: ModelAndView, @PathVariable id: String, @PathVariable param: String,
                      @RequestParam("p", defaultValue = "0") p: Int): ModelAndView {
        val assets: Iterable<Asset>? = when(param) {
            "allsrcIPv4" -> assetDAO.getBySrcIP4(id, p)
            "alldestIPv4" -> assetDAO.getByDestIP4(id, p)
            "allsrcMAC" -> assetDAO.getBySrcMAC(id, p)
            "alldestMAC" -> assetDAO.getByDestMAC(id, p)
            "allsrcPort" -> assetDAO.getBySrcPort(id.toInt(), p)
            "alldestPort" -> assetDAO.getByDestPort(id.toInt(), p)
            "proto" -> assetDAO.getByProto(id, p)
            else -> null
        }
        if(assets==null){
            mav.viewName = "error"
            mav.addObject("message", "sorry, page not found")
            return mav
        }
        mav.viewName = "assetsGroupedOneParam"
        mav.addObject("assets", assets)
        mav.addObject("p", p)
        mav.addObject("type", "src IPv4 = $id")
        return mav
    }

    @GetMapping("/assets/allsrcIPv4")
    fun allsrcIPv4(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedBySrcIp4()
        mav.viewName = "groupSrcIp4"
        mav.addObject("assets", assets)
        return mav
    }

    @GetMapping("/api/assets/allsrcIPv4", produces = [MediaType.APPLICATION_JSON_VALUE])
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

    @GetMapping("/assets/alldestIPv4")
    fun alldestIPv4(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedByDestIp4()
        mav.viewName = "groupDestIp4"
        mav.addObject("assets", assets)
        return mav
    }

    @GetMapping("/api/assets/alldestIPv4", produces = [MediaType.APPLICATION_JSON_VALUE])
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

    @GetMapping("/assets/allsrcMAC")
    fun allsrcMAC(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedBySrcMac()
        mav.viewName = "groupSrcMAC"
        mav.addObject("assets", assets)
        return mav
    }

    @GetMapping("/api/assets/allsrcMAC", produces = [MediaType.APPLICATION_JSON_VALUE])
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

    @GetMapping("/assets/alldestMAC")
    fun alldestMAC(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedByDestMac()
        mav.viewName = "groupDestMAC"
        mav.addObject("assets", assets)
        return mav
    }

    @GetMapping("/api/assets/alldestMAC", produces = [MediaType.APPLICATION_JSON_VALUE])
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

    @GetMapping("/assets")
    fun allAssets(mav: ModelAndView,
                  @RequestParam("pageN", defaultValue = "0") pageN: Int,
                  @RequestParam("order", defaultValue = "0") order: Int): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getAll(pageN, order)
        mav.viewName = "allAssets"
        mav.addObject("assets", assets)
        mav.addObject("pageN", pageN)
        return mav
    }

    @GetMapping("/api/assets", produces = [MediaType.APPLICATION_JSON_VALUE])
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

    @GetMapping("/assets/proto")
    fun allProto(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedByProto()
        mav.viewName = "groupProto"
        mav.addObject("assets", assets)
        return mav
    }
}