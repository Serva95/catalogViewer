package it.srv.catalogViewer

import it.srv.catalogViewer.dao.AuthoritiesDAO
import it.srv.catalogViewer.dao.UserDAO
import it.srv.catalogViewer.dto.UserDTO
import it.srv.catalogViewer.exception.ObjectAlreadyExistException
import it.srv.catalogViewer.exception.PasswordException
import it.srv.catalogViewer.model.Authority
import it.srv.catalogViewer.model.User
import it.srv.catalogViewer.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/userProfile")
class UserController {

    @Autowired
    lateinit var userDAO: UserDAO

    @Autowired
    lateinit var authoritiesDAO: AuthoritiesDAO

    @Autowired
    lateinit var userService: UserService

    @GetMapping("")
    fun userProfile(mav: ModelAndView): ModelAndView {
        val authentication = SecurityContextHolder.getContext().authentication
        val user: User? = userDAO.getByUsername(authentication.name)
        val authList: ArrayList<Authority?> = authoritiesDAO.getByUsername(authentication.name)!!
        var authorities: Array<Authority?> = arrayOfNulls(authList.size)
        authorities = authList.toArray(authorities)
        mav.viewName = "userProfile"
        mav.addObject("user", user)
        mav.addObject("authorities", authorities)
        return mav
    }

    @GetMapping("/{id}")
    fun userProfileEdit(@PathVariable id: Int): ModelAndView {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userDAO.getByUsername(authentication.name)
        val mav = ModelAndView("editUser")
        val userDTO = UserDTO(user!!.username!!, "", "", user.email!!, "")
        mav.addObject("userDTO", userDTO)
        mav.addObject("userId", user.id)
        return mav
    }

    @PutMapping("/{id}")
    fun userProfileEditSave(
        @PathVariable id: Int,
        @ModelAttribute("userDTO") user: UserDTO,
        @RequestParam("matchingPassword") matchingPassword: String?
    ): ModelAndView? {
        var mav = ModelAndView("editUser")
        val authentication = SecurityContextHolder.getContext().authentication
        val actualUser = userDAO.getByUsername(authentication.name)
        user.username = actualUser!!.username
        if (actualUser.id != id) {
            mav = ModelAndView("editUser")
            mav.addObject("userDTO", user)
            mav.addObject("userId", actualUser.id)
            mav.addObject("message", "Ehi che fai ? Puoi modificare solo il tuo profilo!")
            return mav
        }
        try {
            userService.updateAccountData(actualUser, user)
        } catch (e: ObjectAlreadyExistException) {
            mav = ModelAndView("editUser")
            mav.addObject("userDTO", user)
            mav.addObject("userId", actualUser.id)
            mav.addObject("message", e.message)
            return mav
        } catch (e: PasswordException) {
            mav = ModelAndView("editUser")
            mav.addObject("userDTO", user)
            mav.addObject("userId", actualUser.id)
            mav.addObject("message", e.message)
            return mav
        }
        mav.addObject("message", "Modifiche salvate.")
        mav.viewName = "redirect:/userProfile"
        return mav
    }
}