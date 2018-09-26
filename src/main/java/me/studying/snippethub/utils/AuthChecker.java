package me.studying.snippethub.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthChecker {

    public static boolean isAuth() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if(auth.getPrincipal().toString() != "anonymousUser")
        return true;
    return false;
    }
}
