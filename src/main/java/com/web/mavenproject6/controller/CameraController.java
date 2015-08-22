/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.controller;

/**
 *
 * @author Aleks
 */
import static com.web.mavenproject6.controller.UserController.logger;
import com.web.mavenproject6.forms.UserForm;
import com.web.mavenproject6.utility.EncryptionUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import net.tanesha.recaptcha.ReCaptchaImpl;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CameraController {
    
@Qualifier("rootLogger")
@Autowired
private  Logger log;
   
    @Autowired
    private ReCaptchaImpl reCaptcha;

    @Autowired
    EncryptionUtil encryptionUtil;

    @RequestMapping(value = {"/camera"}, method = RequestMethod.GET)
    public String defaultPage(Model model, Locale locale) throws ArithmeticException {
        log.debug("we are here uuu");
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserForm());
        }
        logger.debug("Check: reCaptcha {}", reCaptcha != null);
        if (reCaptcha != null) {
            model.addAttribute("recaptcha", reCaptcha.createRecaptchaHtml(null, null));
        }
        log.debug("we are exit");
        return "thy/camera";

    }

    @ResponseBody
    @RequestMapping(value = "/camera", method = RequestMethod.POST)
    public String upload(@RequestParam(value = "imgdata", required = false) String imgdata,
            HttpServletRequest request) throws FileNotFoundException, IOException, GeneralSecurityException, JSONException {

//        if (imgdata.length() != 256) {
//            JSONObject o = new JSONObject();
//            o.put("error", "Invalid username and password!");
//            return o.toString();
//
//        }
//
//        imgdata = encryptionUtil.decrypt(imgdata.getBytes());
//        System.out.println("imgdata!DECRYPT!" + imgdata);
//        JSONObject o = new JSONObject();
//        o.append("success", "Data success decrypt");
//        return o.toString();
        JSONArray ar = new JSONArray();
        JSONObject resultJson = new JSONObject();
        JSONObject obj = new JSONObject();
   
           
            obj.put("propNumber", "0000001");
            obj.put("propDate", (new Date()).toString());
            obj.put("fname", "Василий");
            obj.put("sname", "Семенович");//
            obj.put("tname", "Казякин");//
            obj.put("pasport", "ВК898999");//
            obj.put("level", "10");
            obj.put("userId", "10");
            ar.add(obj);
       

        resultJson.put("user", ar);
        return resultJson.toString();
    }

    @RequestMapping(value = {"/camera/profile/{userId}"}, method = RequestMethod.GET)
    public ModelAndView getProfile(@PathVariable("userId") String userId) {

        ModelAndView model = new ModelAndView("thy/user/profile");
 
        model.addObject("propNumber", "0000001");
        model.addObject("propStart", "24.05.2015");
        model.addObject("propEnd", "24.05.2016");
        model.addObject("propTname", "Савинков");
        model.addObject("propFname", "Никита");
        model.addObject("propSname", "Алексеевич");
        model.addObject("propDocument", "ВН456223");
        model.addObject("propLevel", "20");
        return model;

    }
    
    @RequestMapping(value = "/camera/avatar/{userId}", method = RequestMethod.GET)
    public @ResponseBody
    BufferedImage getFile(@PathVariable Long userId) throws IOException {
        System.out.println("IMGDATA!"+userId);
        BufferedImage img = ImageIO.read(new File("c:\\1\\Desert.jpg"));
         System.out.println("IMGDATA!loaded");
         
        return img;
    }

    @RequestMapping(value = "/camera/plist", method = RequestMethod.GET)
    public @ResponseBody
    String getPersonalName(@RequestParam("personalName") String personalName) {
        JSONArray ar = new JSONArray();

        for (int i = 0; i < 10; i++) {
            String a = "array" + i;
            if (a.contains(personalName)) {
                ar.add(a);
            }
        }
        return ar.toString();

    }

    @ResponseBody
    @RequestMapping(value = "/camera/logs", method = RequestMethod.POST)
    public String cameraLogs() throws JSONException {
        JSONArray ar = new JSONArray();
        JSONObject resultJson = new JSONObject();
        for (int i = 0; i < 100; i++) {
            JSONObject obj = new JSONObject();
            obj.put("Time", (new Date()).toString());
            obj.put("Message", i % 2 == 0 ? "Acepted" : "Denied");
            obj.put("User", "Vasya" + i);
            obj.put("Type", i % 2 == 0 ? "error" : "success");//
            ar.add(obj);
        }
        resultJson.put("logs", ar);
        return resultJson.toString();

    }
    
    @ResponseBody
    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public String pingPong() throws JSONException {
        return "1";
    }

    @ResponseBody
    @RequestMapping(value = "/camera/guest", method = RequestMethod.GET)
    public String addGuest(Model model) throws JSONException {
        JSONArray ar = new JSONArray();

        JSONObject resultJson = new JSONObject();

        for (int i = 0; i < 20; i++) {
            JSONObject obj = new JSONObject();
            obj.put("time", (new Date()).toString());
            obj.put("message", "error message created for test");
            obj.put("user", "Vasya" + i);
            ar.add(obj);
        }

        resultJson.put("logs", ar);
        return resultJson.toString();
    }
}
