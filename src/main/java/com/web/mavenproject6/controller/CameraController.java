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
import com.web.mavenproject6.entities.guest;
import com.web.mavenproject6.entities.personal;
import com.web.mavenproject6.forms.UserForm;
import com.web.mavenproject6.service.GuestService;
import com.web.mavenproject6.service.PersonalService;
import com.web.mavenproject6.utility.EncryptionUtil;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.tanesha.recaptcha.ReCaptchaImpl;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
    private Logger log;

    @Autowired
    private ReCaptchaImpl reCaptcha;

    @Autowired
    PersonalService personalService;

    @Autowired
    GuestService guestService;

    @Autowired
    EncryptionUtil encryptionUtil;

    @Autowired
    ServletContext servletContext;

    private List<JSONObject> simpleLog = new ArrayList<>();

    @RequestMapping(value = {"/camera"}, method = RequestMethod.GET)
    public String defaultPage(Model model, Locale locale) throws ArithmeticException {
        log.debug("we are here uuu");
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserForm());
        }
        log.debug("Check: reCaptcha " + (reCaptcha != null));
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

        if (StringUtils.isEmpty(imgdata)) {
            //return (new Date()).toString();
        }
        
        
        JSONObject o = new JSONObject(imgdata);
        imgdata = new String(Base64.getDecoder().decode(o.getString("qr")));
       
       
        
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

        simpleLog.add(obj);
        return resultJson.toString();
    }

    @RequestMapping(value = {"/skiper/{userId}"}, method = RequestMethod.GET)
    public ModelAndView getProfile(@PathVariable("userId") String userId) {

        ModelAndView model = new ModelAndView("thy/personal/skiper");
        Object pObject = personalService.findByAccessNumber(userId);
        if (pObject instanceof personal) {

            personal p = (personal) pObject;
            model.addObject("propId", p.getAccessNumber());
            model.addObject("propStart", p.getBegin());
            model.addObject("propEnd", p.getEnd());
            model.addObject("propTname", p.getTname());
            model.addObject("propFname", p.getFname());
            model.addObject("propSname", p.getSname());
            model.addObject("propDocument", p.getPassportNum() + p.getPassportSeria());
            model.addObject("propLevel", p.getStage());

        }

        Object gObject = guestService.findByAccessNumber(userId);
        if (gObject instanceof guest) {

            guest g = (guest) gObject;
            model.addObject("propId", g.getAccessNumber());
            model.addObject("propStart", g.getBegin());
            model.addObject("propEnd", g.getEnd());
            model.addObject("propTname", g.getTname());
            model.addObject("propFname", g.getFname());
            model.addObject("propSname", g.getSname());
            model.addObject("propDocument", g.getPassportNum() + g.getPassportSeria());
            model.addObject("propLevel", "GUEST");

        }

        return model;
    }

    @RequestMapping(value = "/avatar/{propId}", method = RequestMethod.GET)
    public @ResponseBody
    BufferedImage getFile(@PathVariable String propId) throws IOException {

        BufferedImage img;
        byte[] imageInByte;

        Object pObject = personalService.findByAccessNumber(propId);
        if (pObject instanceof personal) {

            imageInByte = ((personal) pObject).getPhoto();
            if (imageInByte.length > 1) {
                InputStream in = new ByteArrayInputStream(imageInByte);
                img = ImageIO.read(in);
            } else {

                InputStream in = servletContext.getResourceAsStream("/resources/img/no_avatar.jpg");
                img = ImageIO.read(in);
            }
            return img;
        }

        Object gObject = guestService.findByAccessNumber(propId);
        if (gObject instanceof guest) {
            imageInByte = ((guest) gObject).getPhoto();
            if (imageInByte.length > 1) {
                InputStream in = new ByteArrayInputStream(imageInByte);
                img = ImageIO.read(in);
            } else {
                InputStream in = servletContext.getResourceAsStream("/resources/img/no_avatar.jpg");
                img = ImageIO.read(in);
            }
            return img;
        }

        InputStream in = servletContext.getResourceAsStream("/resources/img/no_avatar.jpg");
        img = ImageIO.read(in);
        return img;

    }

    @RequestMapping(value = "/avatar/reset/{propId}", method = RequestMethod.GET)
    public @ResponseBody
    void resetFile(@PathVariable String propId) throws IOException {
        Object pObject = personalService.findByAccessNumber(propId);
        if (pObject instanceof personal) {
            personal p = (personal) pObject;
            p.setPhoto(new byte[1]);
            p.setLastUpdate(new Date());
            personalService.getRepository().save(p);
        }

        Object gObject = guestService.findByAccessNumber(propId);
        if (gObject instanceof guest) {
            guest g = (guest) gObject;
            g.setPhoto(new byte[1]);
            g.getPersonal_guest().setLastUpdate(new Date());
            guestService.getRepository().save(g);
        }

    }

    @RequestMapping(value = "/qr/{userId}/", method = RequestMethod.GET)
    public @ResponseBody
    BufferedImage getQRCode(@PathVariable String userId) throws IOException, GeneralSecurityException {
        BufferedImage img;

        Object pObject = personalService.findByAccessNumber(userId);
        if (pObject instanceof personal) {
            personal p = (personal) pObject;
            String userDate = "{userId:"+p.getAccessNumber() + ",fname:" + p.getFname() + ",tname:" + p.getTname()+"}";
            ByteArrayOutputStream out = QRCode.from(Base64.getEncoder()
                    .encodeToString(userDate.getBytes()))
                    .to(ImageType.JPG).stream();
            byte[] imageInByte;
            imageInByte = out.toByteArray();
            InputStream in = new ByteArrayInputStream(imageInByte);
            img = ImageIO.read(in);
            return img;
        }

        Object gObject = guestService.findByAccessNumber(userId);
        if (gObject instanceof guest) {
            guest g = (guest) gObject;
            String userDate = g.getAccessNumber() + " " + g.getFname() + " " + g.getTname();
            ByteArrayOutputStream out = QRCode.from(Base64.getEncoder()
                    .encodeToString(userDate.getBytes()))
                    .to(ImageType.JPG).stream();
            byte[] imageInByte;
            imageInByte = out.toByteArray();
            InputStream in = new ByteArrayInputStream(imageInByte);
            img = ImageIO.read(in);
            return img;
        }

        InputStream in = servletContext.getResourceAsStream("/resources/img/qr-code.jpg");
        img = ImageIO.read(in);
        return img;

    }

    @ResponseBody
    @RequestMapping(value = "/guest/list/{propId}", method = RequestMethod.GET)
    public String guestList(@PathVariable String propId, Model model) throws JSONException {
        JSONArray ar = new JSONArray();
        JSONObject resultJson = new JSONObject();

        Object pObject = (personal) personalService.findByAccessNumber(propId);
        if (pObject instanceof personal) {

            personal p = (personal) pObject;
            resultJson.put("size", p.getGuests().size());
            for (guest g : p.getGuests()) {

                JSONObject obj = new JSONObject();
                obj.put("guestId", g.getId());
                obj.put("fname", g.getFname());
                obj.put("sname", g.getSname());
                obj.put("tname", g.getTname());
                obj.put("passport", g.getPassportSeria() + " " + g.getPassportNum());
                ar.add(obj);
            }
        } else {
            resultJson.put("size", "0");
        }
        resultJson.put("guests", ar);
        return resultJson.toString();
    }

    @RequestMapping(value = "/plist", method = RequestMethod.GET)
    public @ResponseBody
    String getPersonalName(@RequestParam("personalName") String personalName) {
        JSONArray ar = new JSONArray();

        List<personal> pList = personalService.getAll();
        for (personal p : pList) {
            String a = p.getAccessNumber() + " " + p.getFname() + " " + p.getSname() + " " + p.getTname();
            if (a.contains(personalName)) {
                ar.add(p.toString());
            }
        }
        return ar.toString();

    }

    @ResponseBody
    @RequestMapping(value = "/logs", method = RequestMethod.POST)
    public String cameraLogs() throws JSONException {
        JSONArray ar = new JSONArray();
        JSONObject resultJson = new JSONObject();
        for (JSONObject j : simpleLog) {
            ar.add(j);
        }
        resultJson.put("logs", ar);
        return resultJson.toString();

    }

    @ResponseBody
    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public String pingPong() throws JSONException {
        return "pong";
    }

}
