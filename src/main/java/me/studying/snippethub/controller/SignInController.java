package me.studying.snippethub.controller;

import me.studying.snippethub.dao.AppUserDAO;
import me.studying.snippethub.entity.Users;
import me.studying.snippethub.utils.WebUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class SignInController {

//    @RequestMapping(value = "/signin", method = RequestMethod.GET)
//    public String loginPage(Model model) {
//        if(isAuth())
//            return "profilePage";
//        return "signInPage";
//    }
//
//    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
//    public String logoutSuccessfulPage(Model model) {
//        model.addAttribute("title", "Logout");
//        return "welcomePage";
//    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {

        String userName = principal.getName();

        // User loginedUser = (User) ((Authentication) principal).getPrincipal();

        JSONArray ja = WebUtils.getJSONofUserSnippets(WebUtils.getUserID(userName));
        Users user = AppUserDAO.findAppUserByID(WebUtils.getUserID(userName).intValue());
        //  String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userName", userName);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("jsonSnippets", ja);

        return "profilePage";
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public String getUserPage(@PathVariable("userName") String userName,
                              Model model, Principal principal) {

        // String currentUserName = principal.getName();

        String currentUserName = principal == null ? "unknown" : principal.getName();

        if (currentUserName.equals(userName)) {
            return "redirect:/profile";
        }

        JSONArray ja = WebUtils.getJSONofUserSnippets(WebUtils.getUserID(userName));
        Users user = AppUserDAO.findAppUserByID(WebUtils.getUserID(userName).intValue());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("userName", userName);
        model.addAttribute("jsonSnippets", ja);
        return "profilePage";
    }
}
