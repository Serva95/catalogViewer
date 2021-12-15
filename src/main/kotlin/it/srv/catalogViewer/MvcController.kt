package it.srv.catalogViewer

import it.srv.catalogViewer.dao.AssetsDAO
import it.srv.catalogViewer.dao.UserDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class MvcController {

    @Autowired
    lateinit var userDAO: UserDAO

    @Autowired
    lateinit var assetDAO: AssetsDAO

    @GetMapping("/")
    fun viewHomePage(mav: ModelAndView): ModelAndView? {
        mav.viewName = "index"
        return mav
    }

    @GetMapping("/debug")
    fun debug(mav: ModelAndView): ModelAndView? {
        mav.addObject("records", assetDAO.getMaxIdValue())
        mav.addObject("users", userDAO.getAll())
        mav.viewName = "debug"
        return mav
    }
}