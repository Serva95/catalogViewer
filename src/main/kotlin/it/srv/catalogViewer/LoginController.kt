package it.srv.catalogViewer

import it.srv.catalogViewer.dto.UserDTO
import it.srv.catalogViewer.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class LoginController {

    @GetMapping("/login")
    fun viewLoginPage(mav: ModelAndView): ModelAndView {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.name != "anonymousUser") {
            mav.viewName = "redirect:/userProfile"
        } else {
            val user = UserDTO(null, null, null, null, null)
            mav.viewName = "login"
            mav.addObject("user", user)
        }
        return mav
    }
}
