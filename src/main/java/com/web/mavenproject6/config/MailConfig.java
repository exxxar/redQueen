/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.config;

import com.web.mavenproject6.forms.MailForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author Aleks
 */
@Configuration
@PropertySource("resources/application.properties")
public class MailConfig {

    @Value("${mail.subject}")
    private String mailSubject;
    @Value("${mail.from}")
    private String mailFrom;
    @Value("${mail.website.name}")
    private String mailWebsiteName;
    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.smtp.host}")
    private String mailSmtpHost;
    @Value("${mail.smtp.username}")
    private String mailSmtpUsername;
    @Value("${mail.smtp.password}")
    private String mailSmtpPassword;
    @Value("${mail.smtp.port}")
    private String mailSmtpPort;
    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${mail.smtp.starttls.enable}")
    private String mailSmtpStartTlsEnable;
    
    @Bean
    JavaMailSender javaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setPort(Integer.parseInt(mailSmtpPort));
        javaMailSender.setHost(mailSmtpHost);
        javaMailSender.setUsername(mailSmtpUsername);
        javaMailSender.setPassword(mailSmtpPassword);
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", mailSmtpAuth);
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", mailSmtpStartTlsEnable);
        return javaMailSender;
    }

    @Bean
    MailForm mailForm() {
        MailForm mailBean = new MailForm();
        mailBean.setHost(mailHost);
        mailBean.setSubject(mailSubject);
        mailBean.setWebsiteName(mailWebsiteName);
        mailBean.setFrom(mailFrom);
        return mailBean;
    }

}
