/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.*;

import org.apache.log4j.net.SocketAppender;
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
public class Log4j {

    @Value("${log4j.consoleAppender.pattern}")
    private String consolePattern;
    @Value("${log4j.fileAppender.pattern}")
    private String filePattern;
    @Value("${log4j.socketAppender.pattern}")
    private String socketPattern;
    @Value("${log4j.data.format}")
    private String dataFormat;
    @Value("${log4j.fileAppender.extention}")
    private String fileExt;
    @Value("${log4j.fileAppender.maxSize}")
    private long maxSize;
    @Value("${log4j.socketAppender.host}")
    private String host;
    @Value("${log4j.socketAppender.port}")
    private int port;
    @Value("${log4j.socketAppender.delay}")
    private int delay;

    @Bean
    public ConsoleAppender consoleAppender() {
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setThreshold(Level.TRACE);
        PatternLayout patternLayout = new PatternLayout(consolePattern);
        consoleAppender.setLayout(patternLayout);

        return consoleAppender;
    }

    @Bean
    public RollingFileAppender fileAppender() throws IOException {
        PatternLayout patternLayout = new PatternLayout(filePattern);
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(dataFormat);
        RollingFileAppender fileAppender = new RollingFileAppender(patternLayout, "/logs/log-" + ft.format(dNow) + fileExt, true);
        fileAppender.setMaxBackupIndex(2);
        fileAppender.setMaximumFileSize(maxSize);

        return fileAppender;
    }

    @Bean
    public SocketAppender socketAppender() {
        SocketAppender s = new SocketAppender(host, port);
        s.setReconnectionDelay(delay);
        PatternLayout patternLayout = new PatternLayout(socketPattern);
        s.setLayout(patternLayout);
        return s;
    }

    @Bean
    public Logger rootLogger() throws IOException {
        Logger logger = Logger.getRootLogger();
        logger.setLevel(Level.INFO);
        logger.addAppender((Appender) consoleAppender());
        logger.addAppender((Appender) fileAppender());
        logger.addAppender((Appender) socketAppender());
        return logger;
    }

}
