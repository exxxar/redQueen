/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.controller;

import com.web.mavenproject6.entities.Users;
import com.web.mavenproject6.other.UserSessionComponent;
import com.web.mavenproject6.service.UserServiceImp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Aleks
 */
@Controller
public class MainController {

    @Qualifier("rootLogger")
    @Autowired
    private Logger log;

    @Autowired
    private Environment env;

    @Autowired
    UserDetailsService myUserDetailsService;

    @Autowired
    private UserSessionComponent userSessionComponent;
    @Autowired
    private UserServiceImp userService;

    @RequestMapping(value = {"/"})
    public String login(Model model, @RequestParam(required = false) String message) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isSecure = false;
        for (GrantedAuthority role : auth.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_SECURE")) {
                isSecure = true;
            }
        }
        if (isSecure) {
            return "thy/camera";
        }

        UserDetails ud = (UserDetails) auth.getPrincipal();

        Users u = userService.getRepository().findUserByEmail(ud.getUsername());
        if (u == null) {
            u = userService.getRepository().findUserByLogin(ud.getUsername());
        }

        if (u == null) {
            return "thy/error/404";
        }

        model.addAttribute("propId", u.getPerson().getAccessNumber());
        return "thy/personal/profile";
    }

    @RequestMapping(value = {"/self_profile"})
    public String profileXS(Model model, @RequestParam(required = false) String message) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails ud = (UserDetails) auth.getPrincipal();
        System.out.println("!!!!!!12345"+ud.getUsername());
        Users u = userService.getRepository().findUserByEmail(ud.getUsername());
        if (u == null) {
            u = userService.getRepository().findUserByLogin(ud.getUsername());
        }
        System.out.println("!!!!!!123456");
        if (u == null) {
            return "thy/error/404";
        }
        System.out.println("!!!!!!123457");
        model.addAttribute("propId", u.getPerson().getAccessNumber());
        return "thy/personal/profile";
    }

    @RequestMapping("/login/success")
    public String loginSuccess() {
        userSessionComponent.setCurrentUser(userService.getRepository().findUserByLoginOrEmail(
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(),
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
        ));
        return "thy/camera";
    }

    @RequestMapping(value = "/logout/success")
    public String logoutSuccess() {
        System.out.println("logout!");
        return "thy/camera";
    }

    @RequestMapping("/login/error")
    public String loginError(Model model) {
        System.out.println("login error!");
        model.addAttribute("loginError", true);
        return "thy/error/Exception";
    }

    @RequestMapping("/login/profile")
    public String profileUser(Model model) {

        return "thy/public/profile";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public ModelAndView ex404() {
        return new ModelAndView("thy/error/404");
    }

    @RequestMapping(value = "/405", method = RequestMethod.GET)
    public ModelAndView ex405() {
        return new ModelAndView("thy/error/405");
    }

    @RequestMapping(value = "/500", method = RequestMethod.GET)
    public ModelAndView ex500() {
        return new ModelAndView("thy/error/500");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, Model mode) {

        if (logout != null) {
            return "thy/public/logout";
        }
        return "thy/public/login";

    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied() {

        ModelAndView model = new ModelAndView();

        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            model.addObject("username", userDetail.getUsername());
        }
        model.setViewName("thy/error/403");
        return model;

    }

}
