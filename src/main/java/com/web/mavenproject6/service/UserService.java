/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.service;

import com.web.mavenproject6.entities.Users;
import com.web.mavenproject6.repositories.UserRepository;
import java.util.List;

/**
 *
 * @author Aleks
 */
public interface UserService {

    long count();

    Users findOne(Long id);

    Users findUserBySecurityCode(String email, String securityCode);

    boolean isUserExist(String email);

    void save(Users u);

    void remove(Users u);

    void remove(long id);

    void update(Users u);
    
    UserRepository getRepository();

}
