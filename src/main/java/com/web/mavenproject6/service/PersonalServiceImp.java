/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.service;

import com.web.mavenproject6.entities.personal;
import com.web.mavenproject6.repositories.PersonalRepository;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aleks
 */
@Service
public class PersonalServiceImp implements PersonalService {

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private EntityManager em;

    @Override
    public PersonalRepository getRepository() {
        return personalRepository;
    }

    @Override
    public personal findBySNP(String surname, String name, String patronymic) {
        return (personal) em.createQuery("from personal where surname = :surname and"
                + " name = :name and patronymic = :patronymic")
                .setParameter("surname", surname)
                .setParameter("name", name)
                .setParameter("patronymic", patronymic)
                .getSingleResult();
    }

    @Override
    public void add(personal p) {
       personalRepository.save(p);
    }
}
