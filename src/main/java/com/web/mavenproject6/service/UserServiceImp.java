
package com.web.mavenproject6.service;

import com.web.mavenproject6.entities.Users;
import com.web.mavenproject6.repositories.UserRepository;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Aleks
 */

@Service
public class UserServiceImp implements UserService{
    
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public long count() {
       return userRepository.count();
    }

    @Override
    public Users findOne(Long id) {
       return userRepository.findOne(id);
    }

    @Override
    public Users findUserBySecurityCode(String email, String securityCode) {
       TypedQuery query = em.createQuery("select u from users u where u.securityCode.code = ?1 and u.email = ?2", Users.class);
        query.setParameter(1, securityCode)
        .setParameter(2, email);
        return (Users) query.getSingleResult();
    }
    
    @Override
    public boolean isUserExistByEmail(String email) {
        TypedQuery query = em.createQuery("select u from users u where u.email = :email", Users.class)
        .setParameter("email", email);
        try {
           query.getSingleResult();
        }catch(Exception ee){
              System.out.println("signup error USER"+ee);
              return false;
        }
             
    return true;
    }
    
        @Override
    public boolean isUserExistByLogin(String login) {
        TypedQuery query = em.createQuery("select u from users u where u.login = :login", Users.class)
        .setParameter("login", login);
        try {
           query.getSingleResult();
        }catch(Exception ee){
              System.out.println("signup error USER"+ee);
              return false;
        }
             
    return true;
    }

    @Override
 
       public void save(Users u) {
     
                  userRepository.save(u);
      
        
    }

    @Override
    public void remove(Users u) {
        userRepository.delete(u);
    }

    @Override
    public void remove(long id) {
        userRepository.delete(id);
    }

    @Override
    public void update(Users u) {
         userRepository.save(u);
    }

   
    @Override
    public UserRepository getRepository() {
        return userRepository;
    }

    
}
