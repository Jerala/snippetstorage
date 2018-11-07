package me.studying.snippethub.validator;

import me.studying.snippethub.dao.SnippetsDAO;
import me.studying.snippethub.entity.Snippets;
import me.studying.snippethub.formbean.UploadForm;
import me.studying.snippethub.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SnippetsValidator implements Validator {
    @Autowired
    private SnippetsDAO snippetsDAO;

    // The classes are supported by this validator.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UploadForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UploadForm uploadForm = (UploadForm) target;

        // Check the fields of AppUserForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "snippet_name", "NotEmpty.uploadForm.snippet_name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code_text", "NotEmpty.uploadForm.code_text");

        final String regex = "[0-9A-Za-z_ А-Яа-я]";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(uploadForm.getSnippet_name());
        if(!matcher.find())
            errors.rejectValue("snippet_name", "Match.uploadForm.snippet_name");

        if (!errors.hasFieldErrors("snippet_name")) {
            Snippets snippet = snippetsDAO.findSnippetByNameAndUserID(
                    uploadForm.getSnippet_name().replaceAll(" ", "_"),
                    WebUtils.getUserID(null));
            if (snippet != null)
                errors.rejectValue("snippet_name", "Duplicate.uploadForm.snippet_name");
        }
    }
    }
