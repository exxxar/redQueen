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
import com.web.mavenproject6.service.PersonalService;
import com.web.mavenproject6.service.PersonalServiceImp;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
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
            person.setAccessNumber(formatNum(""+user.getId()));
            person.setPhoto(new byte[1]);
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
	public void handleUpload(
            @RequestParam(value = "uploadfile", required = false) MultipartFile uploadfile,
            HttpServletResponse httpServletResponse) {
 
            System.err.println("JSONDATAFILE!!");
        String orgName = uploadfile.getOriginalFilename();
        String filePlaceToUpload = "c:\\1\\";
        System.err.println("JSONDATAFILE!!2"+orgName);
        String filePath = filePlaceToUpload + orgName;
        File dest = new File(filePath);
        try {
            uploadfile.transferTo(dest);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
        
    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    @ResponseBody
    public String userProfileUpdate(@RequestParam("sdata") String sdata)  throws JSONException{
System.out.println("JSONDATA1"+sdata);
        JSONObject o = new JSONObject(sdata);        
        System.out.println("JSONDATA12 "+(String) o.get("propId"));
        personal p = personalService.findByAccessNumber((String) o.get("propId"));          
        System.out.println("JSONDATA2"+sdata);
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
         System.out.println("JSONDATA3"+p.toString());
        personalService.getRepository().save(p);
        return p.getLastUpdate().toString();
    }
    
    @RequestMapping(value = "/profile/info/",method=RequestMethod.POST)
    @ResponseBody
    public String searchContacts(@RequestParam("userData") String userData) throws JSONException {
        System.out.println("JSONDATAdd"+userData);
        personal p = personalService.findByAccessNumber(userData); 
        System.out.println("JSONDATAtest"+p.getFname());
        // System.out.println("JSONDATA"+p.toJSON());
        return p.toString();
    }

    @RequestMapping(value = "/public/activation", method = RequestMethod.GET)
    @Transactional
    public String activation(@RequestParam String mail, @RequestParam String code,Model model) {
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
            model.addAttribute("propId", "000001");
            return "thy/personal/profile";
        }
        log.debug("Exit: activation");
        return "thy/error/error";

    }
    
    private String formatNum(String s){
        if (s.length()<6)
        {
            s="0"+s;
            formatNum(s);
        }
        return s;
    }

}
