package it.srv.catalogViewer

import it.srv.catalogViewer.dto.UserDTO
import it.srv.catalogViewer.exception.ObjectAlreadyExistException
import it.srv.catalogViewer.exception.PasswordException
import it.srv.catalogViewer.exception.UsernameAlreadyExistException
import it.srv.catalogViewer.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class RegistrationController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/register")
    fun viewHomePage(mav: ModelAndView): ModelAndView? {
        val user = UserDTO(null, null, null, null, null)
        mav.viewName = "register"
        mav.addObject("user", user)
        return mav
    }

    @PostMapping("/register")
    fun registerUserAccount(@ModelAttribute("user") user: UserDTO): ModelAndView? {
        try {
            userService.registerNewUserAccount(user)
        } catch (ex: ObjectAlreadyExistException) {
            val mav = ModelAndView("register", "user", user)
            mav.addObject("message", ex.message)
            return mav
        } catch (ex: PasswordException) {
            val mav = ModelAndView("register", "user", user)
            mav.addObject("message", ex.message)
            return mav
        } catch (ex: UsernameAlreadyExistException) {
            val mav = ModelAndView("register", "user", user)
            mav.addObject("message", ex.message)
            return mav
        } catch (ex: RuntimeException) {
            val mav = ModelAndView("error", "user", user)
            mav.addObject("message", ex.message)
            return mav
        }
        val mav = ModelAndView("success", "user", user)
        mav.addObject("message", "Registrazione effettuata. Ora puoi effettuare il login.")
        return mav
    }
}