/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.config;

import net.tanesha.recaptcha.ReCaptchaImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author Aleks
 */
@Configuration
@PropertySource("resources/application.properties")
public class ReCaptchaConfig {

    @Value("${key.private}")
    private String keyPrivate;
    @Value("${key.public}")
    private String keyPublic;
    @Value("${recaptcha.active}")
    private String active;

    @Bean(name = "reCaptcha")
    ReCaptchaImpl reCaptcha() {
        if ("TRUE".equalsIgnoreCase(active)) {
            ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
            reCaptcha.setPrivateKey(keyPrivate);
            reCaptcha.setPublicKey(keyPublic);
            return reCaptcha;
        }
        return null;

    }
}
