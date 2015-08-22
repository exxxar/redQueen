/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.controller;

import com.web.mavenproject6.other.UserSessionComponent;
import com.web.mavenproject6.service.UserServiceImp;
import com.web.mavenproject6.utility.UserTypeEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        model.addAttribute("message", message);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isSecure = false;
        for (GrantedAuthority role : auth.getAuthorities()) {
            if (role.getAuthority().equals("ROLE_"+UserTypeEnum.SECURE.toString()))
                isSecure = true;
        }
        if (isSecure)
            return "thy/camera";
        return "thy/public/profile";
    }

    @RequestMapping("/login/success")
    public String loginSuccess() {
        userSessionComponent.setCurrentUser(userService.getRepository().findUserByLoginOrEmail(
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(),
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
        ));
        return "thy/camera";
    }

    @RequestMapping(value = "/login/failure")
    public String loginFailure(Model model) {
        String message = "Login Failure!";
        System.out.println("login failure!");
        model.addAttribute("loginError", true);
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
        return "thy/camera";
    }
    
     @RequestMapping("/login/profile")
    public String profileUser(Model model) {
        
        return "thy/public/profile";
    }

    @RequestMapping("/error")
    public String loginError() {
        return "thy/error/error";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public ModelAndView ex404() {
        return new ModelAndView("thy/error/404");
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
//           Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//                  if (!(auth instanceof AnonymousAuthenticationToken)){             
//             
//                        return   "redirect:/thy/camera";
//            } 

        return "thy/public/login";

    }

    //for 403 access denied page

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
