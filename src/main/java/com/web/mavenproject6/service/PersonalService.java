/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.mavenproject6.service;

import com.web.mavenproject6.entities.personal;
import com.web.mavenproject6.repositories.PersonalRepository;
import java.util.List;

/**
 *
 * @author Aleks
 */
public interface PersonalService {
    PersonalRepository getRepository();
    personal findByFST(String fname, String sname, String tname);
    Object findByAccessNumber(String accessNumber);
    List<personal> getAll();
    void add(personal p);
}
