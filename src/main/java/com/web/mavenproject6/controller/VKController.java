/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Aleks
 */
@Controller
public class VKController {
    
        @Autowired
        private Environment env;
        
        private static final Logger logger = Logger.getLogger(VKController.class);
        
        @RequestMapping(value="/vkapi")
	public void test(HttpServletResponse response) throws IOException{    
                response.sendRedirect("https://oauth.vk.com/authorize?client_id=4952101&scope=12&redirect_uri=http://localhost:8080/mavenproject6/vk_callback&response_type=code&v=5.34&state=1234&display=popup");
	}
        
        @RequestMapping(value="/vk_callback",method = RequestMethod.GET)
        public void test2(HttpServletRequest r, HttpServletResponse response,ModelMap model) throws IOException, JSONException {
           
            String code = r.getParameter("code");
            String access_token = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet method = new HttpGet("https://oauth.vk.com/access_token?" +
                "client_id="+env.getProperty("vkapi.id")+"&" +
                "client_secret="+env.getProperty("vkapi.secret")+"&" +
                "code="+code+"&" +
                "redirect_uri="+env.getProperty("vkapi.redirect"));
            HttpResponse resp = client.execute(method);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
           JSONObject json = new JSONObject(reader.readLine());       
          
            access_token = json.get("access_token").toString();
            reader.close(); 
     
            
           
            System.err.println("!!!!!!"+access_token);
            
            //return new ModelAndView("test");
        }
        
}
