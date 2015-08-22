/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.mavenproject6.validators;

import com.web.mavenproject6.validators.annotation.MyEmpty;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Aleks
 */
public class MyEmptyValidator implements ConstraintValidator<MyEmpty, String>  {

    private int max = 0;
    private int min = 0;
    
    @Override
    public void initialize(MyEmpty a) {   
      this.max = a.max();
      this.min = a.min();
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext cvc) {
        System.out.print("we are here!");        
        return (t != null && !t.isEmpty())&&(t.length()>=min&&t.length()<max);
    }
    
}
