/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.service;

import com.web.mavenproject6.entities.guest;
import com.web.mavenproject6.entities.personal;
import com.web.mavenproject6.repositories.GuestRepository;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aleks
 */
@Service
public class GuestServiceImp implements GuestService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private EntityManager em;

    @Override
    public GuestRepository getRepository() {
        return guestRepository;
    }

    @Override
    public Object findByAccessNumber(String accessNumber) {
        TypedQuery query = em.createQuery("select g from guest g where g.accessNumber = ?1", guest.class);
        query.setParameter(1, accessNumber);

        try {
            return query.getSingleResult();
        } catch (Exception ee) {
            return false;
        }
    }

    @Override
    public Object findByFST(String fname, String sname, String tname) {
        try {
            return em.createQuery("from guest where fname = :fname and"
                    + " sname = :sname and tname = :tname", guest.class)
                    .setParameter("fname", fname)
                    .setParameter("sname", sname)
                    .setParameter("tname", tname)
                    .getSingleResult();
        } catch (Exception ee) {
            return false;
        }
    }

}
