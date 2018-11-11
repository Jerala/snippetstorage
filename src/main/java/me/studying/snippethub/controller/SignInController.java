package me.studying.snippethub.controller;

import me.studying.snippethub.utils.WebUtils;
import org.json.JSONArray;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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

        String userName = principal.getName();

        System.out.println("User Name: " + userName);

       // User loginedUser = (User) ((Authentication) principal).getPrincipal();

        JSONArray ja = WebUtils.getJSONofUserSnippets(WebUtils.getUserID(userName));

      //  String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userName", userName);
        model.addAttribute("jsonSnippets", ja);

        return "profilePage";
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public String getUserPage(@PathVariable("userName") String userName,
                              Model model, Principal principal) {

       // String currentUserName = principal.getName();

        String currentUserName = principal == null ? "unknown" : principal.getName();

        if(currentUserName.equals(userName)) {
            return "redirect:/profile";
        }

        JSONArray ja = WebUtils.getJSONofUserSnippets(WebUtils.getUserID(userName));

        model.addAttribute("userName", userName);
        model.addAttribute("jsonSnippets", ja);
        return "profilePage";
    }
}
