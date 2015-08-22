/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.mavenproject6.validators;


import com.web.mavenproject6.entities.Users;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Aleks
 */

@Component
public class UsersValidator implements Validator {
    
    @Override
    public boolean supports(Class<?> paramClass) {
        return Users.class.equals(paramClass);
    }
 
    @Override
    public void validate(Object obj, Errors errors) { 
           
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "messages.users.email");
         
//        Users emp = (Users) obj;
//        if(emp.getId() <=0){
//            errors.rejectValue("id", "negativeValue", new Object[]{"'id'"}, "id can't be negative");
//        }
//         
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "role.required");
    }

   
}