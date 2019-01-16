package me.studying.snippethub.controller;

import me.studying.snippethub.dao.PLangsDAO;
import me.studying.snippethub.dao.SnippetsDAO;
import me.studying.snippethub.entity.PLangs;
import me.studying.snippethub.entity.Snippets;
import me.studying.snippethub.formbean.UploadForm;
import me.studying.snippethub.utils.WebUtils;
import me.studying.snippethub.validator.SnippetsValidator;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

import static me.studying.snippethub.utils.AuthChecker.isAuth;

@Controller
public class UploadController {

    @Autowired
    private PLangsDAO pLangsDAO;

    @Autowired
    private SnippetsDAO snippetsDAO;

    @Autowired
    private SnippetsValidator snippetsValidator;

    // Set a form validator
    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        // Form target
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == UploadForm.class) {
            dataBinder.setValidator(snippetsValidator);
        }
        // ...
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String viewRegister(Model model) {

        UploadForm form = new UploadForm();
        List<PLangs> langs = pLangsDAO.getPLangs();

        model.addAttribute("uploadForm", form);
        model.addAttribute("langs", langs);

        return "uploadPage";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String saveRegister(Model model, //
                               @ModelAttribute("uploadForm") @Validated UploadForm uploadForm, //
                               BindingResult result, //
                               final RedirectAttributes redirectAttributes,
                               Principal principal) {

        // Validate result
        if (result.hasErrors()) {
            List<PLangs> langs = pLangsDAO.getPLangs();
            model.addAttribute("langs", langs);
            return "uploadPage";
        }

        Snippets newSnippet = null;
        try {
            newSnippet = snippetsDAO.createSnippet(uploadForm);
        }
        // Other error!!
        catch (Exception e) {
            List<PLangs> langs = pLangsDAO.getPLangs();
            model.addAttribute("langs", langs);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "uploadPage";
        }

        String userName = principal.getName();
        JSONArray ja = WebUtils.getJSONofUserSnippets(WebUtils.getUserID(userName));

        redirectAttributes.addFlashAttribute("userName", userName);
        redirectAttributes.addFlashAttribute("jsonSnippets", ja);
       // redirectAttributes.addFlashAttribute("flashSnippet", newSnippet);

        return "redirect:/profile";
    }

    @RequestMapping("/uploadSuccessful")
    public String viewRegisterSuccessful(Model model) {

        return "profilePage";
    }

}
