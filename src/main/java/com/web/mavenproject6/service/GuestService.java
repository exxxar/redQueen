/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.service;

import com.web.mavenproject6.repositories.GuestRepository;

/**
 *
 * @author Aleks
 */
public interface GuestService {

    GuestRepository getRepository();

    Object findByAccessNumber(String accessNumber);
    Object findByFST(String fname,String sname,String tname);
}
