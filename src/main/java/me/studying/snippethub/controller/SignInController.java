package me.studying.snippethub.controller;

import me.studying.snippethub.utils.WebUtils;
import org.json.JSONArray;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        JSONArray ja = WebUtils.getJSONofUserSnippets();
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < ja.length(); i++) {
                sb.append(ja.getJSONObject(i).toString());
            }
        }
        catch(Exception e) {System.out.println(e);}

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("jsonSnippets", ja);

        return "profilePage";
    }


  /*  @RequestMapping("/profile")
    public String viewProfile(Model model, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);

        JSONArray ja = WebUtils.getJSONofUserSnippets();

        model.addAttribute("jsonSnippets", ja);
        model.addAttribute("userInfo", userInfo);
        return "profilePage";
    }*/
}
