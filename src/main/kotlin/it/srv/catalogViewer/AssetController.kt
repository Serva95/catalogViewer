package it.srv.catalogViewer

import it.srv.catalogViewer.dao.AssetsDAO
import it.srv.catalogViewer.dao.Ipv4InfoDAO
import it.srv.catalogViewer.model.Asset
import it.srv.catalogViewer.services.AssetService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/assets")
class AssetController {

    @Autowired
    lateinit var assetDAO: AssetsDAO

    @Autowired
    lateinit var ipv4InfoDAO: Ipv4InfoDAO

    @Autowired
    lateinit var assetService: AssetService

    @GetMapping("")
    fun allAssets(mav: ModelAndView,
                  @RequestParam("pageN", defaultValue = "0") pageN: Int,
                  @RequestParam("order", defaultValue = "0") order: Int): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getAll(pageN, order)
        mav.viewName = "allAssets"
        mav.addObject("assets", assets)
        mav.addObject("pageN", pageN)
        return mav
    }

    @GetMapping("/allsrcIPv4")
    fun allsrcIPv4(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedBySrcIp4()
        mav.viewName = "groupSrcIp4"
        mav.addObject("assets", assets)
        return mav
    }

    @GetMapping("/alldestIPv4")
    fun alldestIPv4(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedByDestIp4()
        mav.viewName = "groupDestIp4"
        mav.addObject("assets", assets)
        return mav
    }

    @GetMapping("/allsrcMAC")
    fun allsrcMAC(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedBySrcMac()
        mav.viewName = "groupSrcMAC"
        mav.addObject("assets", assets)
        return mav
    }

    @GetMapping("/alldestMAC")
    fun alldestMAC(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedByDestMac()
        mav.viewName = "groupDestMAC"
        mav.addObject("assets", assets)
        return mav
    }

    @GetMapping("/proto")
    fun allProto(mav: ModelAndView): ModelAndView {
        val assets: Iterable<Asset>? = assetDAO.getGroupedByProto()
        mav.viewName = "groupProto"
        mav.addObject("assets", assets)
        return mav
    }

    @GetMapping("/com/{type}")
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

    @GetMapping("/{param}/{id_url}")
    fun singleParamComunication(mav: ModelAndView, @PathVariable id_url: String, @PathVariable param: String,
                      @RequestParam("p", defaultValue = "0") p: Int): ModelAndView {
        val id: String? = if (id_url.compareTo("null", true) == 0)
            null
        else
            id_url
        val assets: Iterable<Asset>? = when(param) {
            "allsrcIPv4" -> assetDAO.getBySrcIP4(id, p)
            "alldestIPv4" -> assetDAO.getByDestIP4(id, p)
            "allsrcMAC" -> assetDAO.getBySrcMAC(id, p)
            "alldestMAC" -> assetDAO.getByDestMAC(id, p)
            "allsrcPort" -> assetDAO.getBySrcPort(id!!.toInt(), p)
            "alldestPort" -> assetDAO.getByDestPort(id!!.toInt(), p)
            "proto" -> assetDAO.getByProto(id!!, p)
            else -> null
        }
        mav.viewName = "assetsGroupedOneParam"
        if(assets==null){
            mav.viewName = "error"
            mav.addObject("message", "Sorry, page not found :(")
            return mav
        } else if (param=="allsrcIPv4" && id!=null) {
            val ipv4Info = ipv4InfoDAO.getByIp(id)
            if (ipv4Info==null) {
                val details = runBlocking {assetService.getIpv4Info(id)}
                mav.addObject("ipinfo", details)
            } else {
                mav.addObject("ipinfo", ipv4Info)
            }
            mav.viewName = "assetsSrcIpv4"
        }
        mav.addObject("assets", assets)
        mav.addObject("p", p)
        mav.addObject("type", "src IPv4 = $id")
        return mav
    }
}