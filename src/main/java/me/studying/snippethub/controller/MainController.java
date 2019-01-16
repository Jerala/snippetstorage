package me.studying.snippethub.controller;

import me.studying.snippethub.dao.PLangsDAO;
import me.studying.snippethub.dao.QueriesDAO;
import me.studying.snippethub.entity.Queries;
import me.studying.snippethub.entity.Snippets;
import me.studying.snippethub.service.SnippetsServiceImpl;
import me.studying.snippethub.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;


@Controller
public class MainController {

    private SnippetsServiceImpl snippetsService;

    @Autowired
    private QueriesDAO queriesDAO;

    @Autowired
    private PLangsDAO pLangsDAO;

    @Autowired
    public MainController(SnippetsServiceImpl snippetFinderService, QueriesDAO queriesDAO, PLangsDAO pLangsDAO) {
        this.snippetsService = snippetFinderService;
        this.queriesDAO = queriesDAO;
        this.pLangsDAO = pLangsDAO;
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
        snippetsService.registerSearchQuery(tag.toLowerCase());
        return snippetsService.findSnippetByTag(tag.toLowerCase());
    }

    @RequestMapping(value = "/getMostPopularQueries", method = RequestMethod.GET)
    @ResponseBody
    public List<Queries> getMostPopularQueries() {
        return queriesDAO.getMostPopularQueries();
    }

    @RequestMapping(value = "/isAuth", method = RequestMethod.GET)
    @ResponseBody
    public String getUserName(Principal principal) {
        return principal == null ? "no" : principal.getName();
    }

    @RequestMapping(value = "/getPLName", method = RequestMethod.GET)
    @ResponseBody
    public String getPLName(@RequestParam(value = "plId") Long plId) {
        return pLangsDAO.findPLByID(plId).getPLName();
    }
}