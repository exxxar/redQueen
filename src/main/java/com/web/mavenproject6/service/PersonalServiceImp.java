/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.service;

import com.web.mavenproject6.entities.Users;
import com.web.mavenproject6.entities.personal;
import com.web.mavenproject6.repositories.PersonalRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
    public personal findByFST(String fname, String sname, String tname) {
        return (personal) em.createQuery("from personal where fname = :fname and"
                + " sname = :sname and tname = :tname")
                .setParameter("fname", fname)
                .setParameter("sname", sname)
                .setParameter("tname", tname)
                .getSingleResult();
    }

    @Override
    public void add(personal p) {
        personalRepository.save(p);
    }

    @Override
    public Object findByAccessNumber(String accessNumber) {
        TypedQuery query = em.createQuery("select p from personal p where p.accessNumber = ?1", personal.class);
        query.setParameter(1, accessNumber);

        try {
            return query.getSingleResult();
        } catch (Exception ee) {
            return false;
        }
    }

    @Override
    public List<personal> getAll() {
        return (List<personal>) personalRepository.findAll();
    }

}
