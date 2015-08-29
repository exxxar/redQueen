/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.controller;

import com.web.mavenproject6.entities.Role;
import com.web.mavenproject6.entities.SecurityCode;
import com.web.mavenproject6.entities.Users;
import com.web.mavenproject6.entities.guest;
import com.web.mavenproject6.entities.personal;
import com.web.mavenproject6.forms.UserForm;
import com.web.mavenproject6.other.UserSessionComponent;
import com.web.mavenproject6.repositories.SecurityCodeRepository;
import com.web.mavenproject6.service.GuestService;
import com.web.mavenproject6.service.MailSenderService;
import com.web.mavenproject6.service.MyUserDetailsService;
import com.web.mavenproject6.service.PersonalService;
import com.web.mavenproject6.service.UserServiceImp;
import com.web.mavenproject6.utility.SecureUtility;
import com.web.mavenproject6.utility.TypeActivationEnum;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Aleks
 */
@Controller
public class UserController {

    @Qualifier("rootLogger")
    @Autowired
    private org.apache.log4j.Logger log;

    @Autowired
    Environment env;

    @Autowired
    UserServiceImp userService;

    @Autowired
    PersonalService personalService;

    @Autowired
    GuestService guestService;

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

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserForm());
        }

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
            ServletRequest servletRequest) throws GeneralSecurityException {

        if (reCaptcha != null) {
            String remoteAdress = servletRequest.getRemoteAddr();
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAdress, challangeField, responseField);
            if (!reCaptchaResponse.isValid()) {
                this.create(model);
                return "thy/public/signup";
            }
        }
        if (!result.hasErrors()) {
            if (userService.isUserExistByEmail(form.getEmail())) {
                FieldError fieldError = new FieldError("user", "email", "email already exists");
                result.addError(fieldError);
                return "thy/public/signup";
            }

            if (userService.isUserExistByLogin(form.getEmail())) {
                FieldError fieldError = new FieldError("user", "username", "login already exists");
                result.addError(fieldError);
                return "thy/public/signup";
            }

            Users user = new Users();

            user.setLogin(form.getUsername());
            user.setEmail(form.getEmail());
            user.setEnabled(false);
            user.setPassword(form.getPassword());

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
            person.setPhoto(new byte[1]);
            user.setPerson(person);
            userService.save(user);

            /* for generate accessNumber by userId */
            user = userService.getRepository().findUserByEmail(user.getEmail());
            person = user.getPerson();
            person.setAccessNumber(formatNum("" + user.getId()));
            user.setPerson(person);
            userService.save(user);

            securityCodeRepository.save(securityCode);
            mailSenderService.sendAuthorizationMail(user, user.getSecurityCode());

        } else {
            this.create(model);
            return "thy/public/signup";

        }
        return "thy/public/mailSent";
    }
//

    @ResponseBody
    @RequestMapping(value = "/profile/upload", method = RequestMethod.POST)
    public String handleUpload(
            @RequestParam(value = "uploadfile", required = false) MultipartFile uploadfile,
            @RequestParam(value = "propId") String propId,
            HttpServletResponse httpServletResponse) throws IOException {

        Object pObject = personalService.findByAccessNumber(propId);
        if (pObject instanceof personal) {
            personal person = (personal) pObject;
            person.setLastUpdate(new Date());
            person.setPhoto(uploadfile.getBytes());
            personalService.getRepository().save(person);
            return "success";
        }

        Object gObject = guestService.findByAccessNumber(propId);
        if (gObject instanceof guest) {
            guest g = (guest) gObject;
            g.getPersonal_guest().setLastUpdate(new Date());
            g.setPhoto(uploadfile.getBytes());
            guestService.getRepository().save(g);
            return "success";
        }

        return "failed";
    }

    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    @ResponseBody
    public String userProfileUpdate(@RequestParam("sdata") String sdata) throws JSONException {

        if (StringUtils.isEmpty(sdata)) {
            return (new Date()).toString();
        }

        JSONObject o = new JSONObject(sdata);
        Object pObject = personalService.findByAccessNumber((String) o.get("propId"));
        if (pObject instanceof personal) {
            personal p = (personal) pObject;
            p.setFname((String) o.get("fname"));
            p.setSname((String) o.get("sname"));
            p.setTname((String) o.get("tname"));
            p.setPassportNum("-");
            p.setPassportSeria((String) o.get("pasport"));
            p.setAddres((String) o.get("address"));
            p.setInfo((String) o.get("comment"));
            p.setPhone((String) o.get("phone"));
            p.setStage((String) o.get("stage"));
            p.setOffice((String) o.get("office"));
            p.setPost((String) o.get("post"));
            p.setLastUpdate(new Date());
            personalService.getRepository().save(p);
            return p.getLastUpdate().toString();
        }

        Object gObject = guestService.findByAccessNumber((String) o.get("propId"));
        if (gObject instanceof guest) {
            guest g = (guest) gObject;
            g.setFname((String) o.get("fname"));
            g.setSname((String) o.get("sname"));
            g.setTname((String) o.get("tname"));
            g.setPassportNum("-");
            g.setPassportSeria((String) o.get("pasport"));
            g.getPersonal_guest().setLastUpdate(new Date());
            guestService.getRepository().save(g);
            return g.getPersonal_guest().getLastUpdate().toString();
        }
        return (new Date()).toString();
    }

    @RequestMapping(value = "/guest/add/{propId}", method = RequestMethod.POST)
    @ResponseBody
    public String addGuest(@PathVariable("propId") String userId, @RequestParam("sdata") String sdata) throws JSONException {

        if (StringUtils.isEmpty(sdata)) {
            return (new Date()).toString();
        }

        JSONObject o = new JSONObject(sdata);
        Object pObject = personalService.findByAccessNumber((String) o.get("propId"));

        guest g = new guest();
        g.setFname((String) o.get("fname"));
        g.setSname((String) o.get("sname"));
        g.setTname((String) o.get("tname"));
        g.setPassportNum("-");
        g.setPassportSeria((String) o.get("pasport"));
        g.setPersonal_guest((personal) pObject);
        ((personal) pObject).addGuest(g);
        ((personal) pObject).setLastUpdate(new Date());
        personalService.getRepository().save(((personal) pObject));
        guestService.getRepository().save(g);

        g = (guest) guestService.findByFST(g.getFname(), g.getSname(), g.getTname());
        g.setAccessNumber("g" + formatNum("" + g.getId()));
        guestService.getRepository().save(g);

        
        return g.getPersonal_guest().getLastUpdate().toString();

    }

    @RequestMapping(value = "/profile/test", method = RequestMethod.POST)
    @ResponseBody
    public String userProfileTest(@RequestParam("userData") String propId) {
        Object pObject = personalService.findByAccessNumber(propId);
        if (pObject instanceof personal) {
            return ((personal) pObject).getLastUpdate().toString();
        }
        return (new Date()).toString();
    }

    @RequestMapping(value = "/profile/info/", method = RequestMethod.POST)
    @ResponseBody
    public String searchContacts(@RequestParam("userData") String userData) throws JSONException {
        Object pObject = personalService.findByAccessNumber(userData);
        return ((personal) pObject).toString();
    }

    @RequestMapping(value = "/public/activation", method = RequestMethod.GET)
    @Transactional
    public String activation(@RequestParam String mail, @RequestParam String code, Model model) {
        log.debug("Enter: activation");
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
            log.debug("Exit: activation");
            model.addAttribute("propId", user.getPerson().getAccessNumber());
            return "thy/personal/profile";
        }
        log.debug("Exit: activation");
        return "thy/error/error";

    }

    private String formatNum(String s) {
        if (s.length() < 6) {
            s = "0" + s;
            System.out.println("recursiya:" + s);
            s = formatNum(s);
        }
        return s;
    }

}
