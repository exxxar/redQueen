/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.mavenproject6.service;

import com.web.mavenproject6.entities.personal;
import com.web.mavenproject6.repositories.PersonalRepository;

/**
 *
 * @author Aleks
 */
public interface PersonalService {
    PersonalRepository getRepository();
    personal findBySNP(String surname, String name, String patronymic);
    void add(personal p);
}
