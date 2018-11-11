package me.studying.snippethub.controller;

import me.studying.snippethub.entity.Snippets;
import me.studying.snippethub.entity.Users;
import me.studying.snippethub.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    AdminServiceImpl adminService;

    @Autowired
    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(value = "/adminPanel", method = RequestMethod.GET)
    public String getAdminPage() {
        return "adminPage";
    }

    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    @ResponseBody
    public List<Users> getUsers() {
        return adminService.getUsers();
    }

    @RequestMapping(value = "/deleteUserById", method = RequestMethod.GET)
    @ResponseBody
    public String deleteUserById(@RequestParam(value = "id") Long id) {
        return adminService.deleteUserById(id) ? "success" : "failed";
    }

    @RequestMapping(value = "/approveSnippet", method = RequestMethod.GET)
    @ResponseBody
    public String approveSnippet(@RequestParam(value = "userId") Long userId,
                                 @RequestParam(value = "snippetName") String snippetName) {
        return adminService.approveSnippet(userId, snippetName);
    }

    @RequestMapping(value = "/updateSnippet", method = RequestMethod.GET)
    @ResponseBody
    public String updateSnippet(@RequestBody Snippets snippet) {
        return adminService.updateSnippet(snippet);
    }

    @RequestMapping(value = "/deleteSnippet", method = RequestMethod.GET)
    @ResponseBody
    public String deleteSnippet(@RequestParam(value = "userId") Long userId,
                                @RequestParam(value = "snippetName") String snippetName) {
        return adminService.deleteSnippet(userId, snippetName);
    }

//    @RequestMapping(value = "/forbidById", method = RequestMethod.GET)
//    @ResponseBody
//    public String forbidUserById(@RequestParam(value = "id") Long user_id) {
//        return adminService.forbidUserById(user_id);
//    }
}
