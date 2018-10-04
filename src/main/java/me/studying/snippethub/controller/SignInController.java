package me.studying.snippethub.controller;

import me.studying.snippethub.utils.WebUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

import static me.studying.snippethub.utils.AuthChecker.isAuth;

@Controller
public class SignInController {

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String loginPage(Model model) {
        if(isAuth())
            return "profilePage";
        return "signInPage";
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {

        // (1) (en)
        // After user login successfully.
        String userName = principal.getName();

        System.out.println("User Name: " + userName);

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "profilePage";
    }


    @RequestMapping("/profile")
    public String viewProfile(Model model, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        return "profilePage";
    }
}
