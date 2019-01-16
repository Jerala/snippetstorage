package me.studying.snippethub.controller;

import me.studying.snippethub.dao.AppUserDAO;
import me.studying.snippethub.entity.Users;
import me.studying.snippethub.utils.snippetUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class SnippetsController {

    @RequestMapping("/snippets/{PL_NAME}/{USER_ID}-{SNIPPET_NAME}")
    public String openSnippet(@PathVariable("PL_NAME") String pl_name,
                              @PathVariable("USER_ID") int user_id,
                              @PathVariable("SNIPPET_NAME") String snippet_name,
                              Model model) {
        HashMap<String, Object> snippetData = snippetUtils.getSnippetData(user_id, snippet_name);
        String text = snippetUtils.getSnippetText(pl_name, user_id, snippet_name);
        Users user = AppUserDAO.findAppUserByID(user_id);
        model.addAttribute("user_name", user.getUserName());
        model.addAttribute("like_count", snippetData.get("like_count"));
        model.addAttribute("codeText", text);
        model.addAttribute("user_id", user_id);
        model.addAttribute("snippet_name", snippet_name);
        model.addAttribute("tags", snippetData.get("tags"));
        return "snippetPage";
    }

    @RequestMapping("/snippets/{USER_ID}-{SNIPPET_NAME}")
    @ResponseBody
    public HashMap<String, Object> getMetaData(@PathVariable("USER_ID") int user_id,
                              @PathVariable("SNIPPET_NAME") String snippet_name) {
        return snippetUtils.getSnippetData(user_id, snippet_name);
    }
}
