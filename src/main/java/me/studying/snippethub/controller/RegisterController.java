package me.studying.snippethub.controller;

import me.studying.snippethub.dao.AppRoleDAO;
import me.studying.snippethub.dao.AppUserDAO;
import me.studying.snippethub.entity.UserRole;
import me.studying.snippethub.formbean.AppUserForm;
import me.studying.snippethub.entity.Users;
import me.studying.snippethub.service.UserDetailsServiceImpl;
import me.studying.snippethub.validator.AppUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static me.studying.snippethub.utils.AuthChecker.isAuth;

@Controller
public class RegisterController {

    @Autowired
    private AppUserDAO appUserDAO;

    @Autowired
    private AppUserValidator appUserValidator;

    // Set a form validator
    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        // Form target
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == AppUserForm.class) {
            dataBinder.setValidator(appUserValidator);
        }
        // ...
    }

    @RequestMapping("/registerSuccessful")
    public String viewRegisterSuccessful(Model model) {

        return "welcomePage";
    }

    // Show Register page.
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String viewRegister(Model model) {

        if(isAuth())
            return "profilePage";

        AppUserForm form = new AppUserForm();

        model.addAttribute("appUserForm", form);

        return "registerPage";
    }

    // This method is called to save the registration information.
    // @Validated: To ensure that this Form
    // has been Validated before this method is invoked.
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveRegister(Model model, //
                               @ModelAttribute("appUserForm") @Validated AppUserForm appUserForm, //
                               BindingResult result, //
                               final RedirectAttributes redirectAttributes) {

        // Validate result
        if (result.hasErrors()) {
            return "registerPage";
        }
        Users newUser = null;
        UserRole ur = null;
        try {
            newUser = appUserDAO.createAppUser(appUserForm);
            AppRoleDAO.createUserRole(newUser.getUserId());
        }
        // Other error!!
        catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "registerPage";
        }

        redirectAttributes.addFlashAttribute("flashUser", newUser);

        return "redirect:/registerSuccessful";
    }

    @RequestMapping(value = "/termOfService/termOfService.txt", method = RequestMethod.GET)
    public String getTermOfService(Model model) {

        String licenseText = UserDetailsServiceImpl.getLicenseText();
        model.addAttribute("licenseText", licenseText);
        return "termOfServicePage";
    }
}
