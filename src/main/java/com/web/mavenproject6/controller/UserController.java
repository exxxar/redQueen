/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.controller;

import com.web.mavenproject6.entities.Role;
import com.web.mavenproject6.entities.SecurityCode;
import com.web.mavenproject6.entities.Users;
import com.web.mavenproject6.entities.personal;
import com.web.mavenproject6.forms.UserForm;
import com.web.mavenproject6.other.UserSessionComponent;
import com.web.mavenproject6.repositories.SecurityCodeRepository;
import com.web.mavenproject6.service.MailSenderService;
import com.web.mavenproject6.service.MyUserDetailsService;
import com.web.mavenproject6.service.UserServiceImp;
import com.web.mavenproject6.utility.SecureUtility;
import com.web.mavenproject6.utility.TypeActivationEnum;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.validation.Valid;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Aleks
 */
@Controller
public class UserController {

    static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserServiceImp userService;

    @Autowired
    SecurityCodeRepository securityCodeRepository;

    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    UserDetailsService myUserDetailsService;

    @Autowired
    private UserSessionComponent userSessionComponent;

    @Autowired
    private ReCaptchaImpl reCaptcha;

    private static List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>(1) {
        {
            add(new GrantedAuthorityImpl("ROLE_USER"));
        }
    };

    public void setMyUserDetailsService(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @RequestMapping("/public/signup")
    public String create(Model model) {
        logger.debug("Enter: create");
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserForm());
        }
        logger.debug("Check: reCaptcha {}", reCaptcha != null);
        if (reCaptcha != null) {
            model.addAttribute("recaptcha", reCaptcha.createRecaptchaHtml(null, null));
        }
        return "thy/public/signup";
    }

    @RequestMapping(value = "/public/signup_confirm", method = RequestMethod.POST)
    @Transactional
    public String createUser(Model model,
            @ModelAttribute("user") @Valid UserForm form, BindingResult result,
            @RequestParam(value = "recaptcha_challenge_field", required = false) String challangeField,
            @RequestParam(value = "recaptcha_response_field", required = false) String responseField,
            ServletRequest servletRequest) {

        logger.debug("Enter: createUser");
        if (reCaptcha != null) {
            String remoteAdress = servletRequest.getRemoteAddr();
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAdress, challangeField, responseField);
            if (!reCaptchaResponse.isValid()) {
                this.create(model);
                return "thy/public/signup";
            }
        }
        if (!result.hasErrors()) {

            // check if email already exists
            logger.debug("signup error 0 " + form.getEmail());
            if (userService.isUserExist(form.getEmail())) {
                logger.debug("signup error 0 1" + form.getEmail());
                FieldError fieldError = new FieldError("user", "email", "email already exists");
                result.addError(fieldError);
                return "thy/public/signup";
            }
            Users user = new Users();
            Md5PasswordEncoder encoder = new Md5PasswordEncoder();
            user.setLogin(form.getUsername());
            user.setEmail(form.getEmail());
            user.setEnabled(false);
            user.setPassword(encoder.encodePassword(form.getPassword(), user.getEmail()));

            Role role = new Role();
            role.setUser(user);
            role.setRole(2);

            SecurityCode securityCode = new SecurityCode();
            securityCode.setUser(user);
            securityCode.setTimeRequest(new Date());
            securityCode.setTypeActivationEnum(TypeActivationEnum.NEW_ACCOUNT);
            securityCode.setCode(SecureUtility.generateRandomCode());
            user.setRole(role);
            user.setSecurityCode(securityCode);

            personal person = new personal();
            person.setUser(user);
            person.setPhoto(new byte[0]);
            user.setPerson(person);
            userService.save(user);
            securityCodeRepository.save(securityCode);
            mailSenderService.sendAuthorizationMail(user, user.getSecurityCode());

        } else {
            this.create(model);
            return "thy/public/signup";

        }
        logger.debug("Exit: createUser");
        return "thy/public/mailSent";
    }

    @RequestMapping(value = "/user/profile")
    public String userProfile() {
        return "thy/user/profile";
    }

    @RequestMapping(value = "/user/searchContact")
    public String searchContact() {
        return "thy/user/searchContact";
    }

//    @RequestMapping(value = "/pages/base/user/get",
//            method = RequestMethod.GET)
//    public @ResponseBody
//    Map<String, String> get() {
//        List<String> usernameList = userRepository.findUsername("");
//        Map<String, String> result = new HashMap<>();
//        for (String username : usernameList) {
//            result.put("label", username);
//
//        }
//        return result;
//    }
    @RequestMapping(value = "/public/activation", method = RequestMethod.GET)
    @Transactional
    public String activation(@RequestParam String mail, @RequestParam String code) {
        logger.debug("Enter: activation");
        if (userService.findUserBySecurityCode(mail, code) != null) {
            Users user = userService.getRepository().findUserByEmail(mail);
            user.setEnabled(true);
            securityCodeRepository.delete(user.getSecurityCode());
            user.setSecurityCode(null);
            userService.save(user);
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getEmail());
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), AUTHORITIES);
            SecurityContextHolder.getContext().setAuthentication(auth);
            userSessionComponent.setCurrentUser(user);
            logger.debug("Exit: activation");
            return "thy/user/profile";
        }
        logger.debug("Exit: activation");
        return "thy/error/error";

    }

}
