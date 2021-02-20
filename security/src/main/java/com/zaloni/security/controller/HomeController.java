package com.zaloni.security.controller;

import com.zaloni.security.oauth2.GithubOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public ModelAndView index() {
        Map<String, Object> model = new HashMap();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GithubOAuth2User user = new GithubOAuth2User((OAuth2User) authentication.getPrincipal());
        model.put("user", user);
        return new ModelAndView("home/index", model);
    }
}
