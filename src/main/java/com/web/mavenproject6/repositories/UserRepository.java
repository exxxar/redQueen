/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.repositories;

import com.web.mavenproject6.entities.Users;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Aleks
 */
public interface UserRepository extends CrudRepository<Users, Long>{


    String findPasswordByLogin(String login);

    String findPasswordByEmail(String email);

    String findEmailByLogin(String login);

    String findLoginByEmail(String email);

    Users findUserByLogin(String login);

    Users findUserByEmail(String email);
    
    Users findUserById(long id);

    Users findUserByLoginOrEmail(String login, String email);

    
}
