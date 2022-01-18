package it.srv.catalogViewer

import it.srv.catalogViewer.dao.AssetsDAO
import it.srv.catalogViewer.model.Asset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/assets")
class AssetController {

    @Autowired
    lateinit var assetDAO: AssetsDAO

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

    @GetMapping("/{param}/{id}")
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
            mav.addObject("message", "Sorry, page not found :(")
            return mav
        }
        mav.viewName = "assetsGroupedOneParam"
        mav.addObject("assets", assets)
        mav.addObject("p", p)
        mav.addObject("type", "src IPv4 = $id")
        return mav
    }
}