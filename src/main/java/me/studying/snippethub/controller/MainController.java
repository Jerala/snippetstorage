package me.studying.snippethub.controller;

import me.studying.snippethub.entity.Snippets;
import me.studying.snippethub.service.SnippetFinderServiceImpl;
import me.studying.snippethub.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.sql.ResultSet;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MainController {

    SnippetFinderServiceImpl snippetFinderService;

    @Autowired
    public MainController(SnippetFinderServiceImpl snippetFinderService) {
        this.snippetFinderService = snippetFinderService;
    }

    @RequestMapping("/")
    public String viewHome(Model model) {
        return "welcomePage";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "403Page";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public List<Snippets> findSnippet(@RequestParam(value = "tagName", required = true,
    defaultValue = ".") String tag) {
        return snippetFinderService.findSnippetByTag(tag);
    }
}