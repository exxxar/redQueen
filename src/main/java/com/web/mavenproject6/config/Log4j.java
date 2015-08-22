/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.config;

import com.web.mavenproject6.controller.CameraController;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.apache.log4j.*;

import org.apache.log4j.net.SMTPAppender;
import org.apache.log4j.net.SocketAppender;
import org.apache.log4j.varia.LevelRangeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.Log4jWebConfigurer;

/**
 *
 * @author Aleks
 */
@Configuration
public class Log4j {

    @Bean
    public ConsoleAppender consoleAppender() {
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setThreshold(Level.TRACE);
        PatternLayout patternLayout = new PatternLayout("%d %-5p  [%c{1}] %m %n");
        consoleAppender.setLayout(patternLayout);

        return consoleAppender;
    }

    @Bean
    public RollingFileAppender fileAppender() throws IOException {
        PatternLayout patternLayout = new PatternLayout("%d %-5p  [%c{1}] %m %n");
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        RollingFileAppender fileAppender = new RollingFileAppender(patternLayout, "c://1/log-" + ft.format(dNow) + ".log", true);
        fileAppender.setMaxBackupIndex(2);
        fileAppender.setMaximumFileSize(100000000000l);
        
        return fileAppender;
    }

    @Bean
    public SocketAppender socketAppender(){
        SocketAppender s = new SocketAppender("localhost", 5000);
        s.setReconnectionDelay(10000);
        PatternLayout patternLayout = new PatternLayout("%m%n");
        s.setLayout(patternLayout);
        return s;
    }

    @Bean
    public Logger rootLogger() throws IOException {
        Logger logger = Logger.getRootLogger();
        logger.setLevel(Level.DEBUG);
        logger.addAppender((Appender) consoleAppender());
        logger.addAppender((Appender) fileAppender());
        logger.addAppender((Appender) socketAppender());
        return logger;
    }

}
