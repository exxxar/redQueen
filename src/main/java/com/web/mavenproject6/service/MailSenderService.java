/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.mavenproject6.service;

/**
 *
 * @author Aleks
 */
import com.web.mavenproject6.entities.SecurityCode;
import com.web.mavenproject6.entities.Users;
import com.web.mavenproject6.forms.MailForm;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailSenderService {

    @Autowired
    private Environment env;
    
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private MailForm mailForm;

    private static String CONFIRMATION_TEMPLATE = "confirm_account.html";

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Async
    public void sendAuthorizationMail(final Users user, final SecurityCode securityCode) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setSubject(mailForm.getSubject());
                message.setTo(user.getEmail());
                message.setFrom(mailForm.getFrom());
                Map model = new HashMap();
                model.put("websiteName", mailForm.getWebsiteName());
                model.put("host", mailForm.getHost());
                model.put("user", user);
                model.put("securityCode", securityCode);
                model.put("projectName",env.getProperty("projectName"));
                System.out.println("MAIL!!:"+user.toString());
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, CONFIRMATION_TEMPLATE, model);
                message.setText(text, true);
            }
        };
        this.mailSender.send(preparator);
    }
}