/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.mavenproject6.service;

import com.web.mavenproject6.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aleks
 */
@Service
public class GuestServiceImp implements GuestService{

    @Autowired
    private GuestRepository guestRepository;
    
    @Override
    public GuestRepository getRepository() {
        return guestRepository;
    }
    
}
