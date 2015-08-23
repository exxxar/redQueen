/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.service;

/**
 *
 * @author Aleks
 */
import com.web.mavenproject6.entities.Users;
import com.web.mavenproject6.utility.EncryptionUtil;
import com.web.mavenproject6.utility.UserTypeEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    EncryptionUtil encryptionUtil;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {

            Users domainUser = (Users) entityManager
                    .createQuery("select u from users u where u.login = :login or u.email = :email")
                    .setParameter("email", login)
                    .setParameter("login", login)
                    .getSingleResult();

            boolean enabled = true;
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;          
      
            return new org.springframework.security.core.userdetails.User(
                    domainUser.getEmail(),
                    domainUser.getPassword(),
                    enabled,
                    accountNonExpired,
                    credentialsNonExpired,
                    accountNonLocked,
                    getAuthorities(domainUser.getRole().getRole()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a collection of {@link GrantedAuthority} based on a numerical
     * role
     *
     * @param role the numerical role
     * @return a collection of {@link GrantedAuthority
     */
    public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
        return authList;
    }

    /**
     * Converts a numerical role to an equivalent list of roles
     *
     * @param role the numerical role
     * @return list of roles as as a list of {@link String}
     */
    public List<String> getRoles(Integer role) {
        List<String> roles = new ArrayList<>();
        switch (role) {
            case 1:
                roles.add(UserTypeEnum.USER.toString());
                roles.add(UserTypeEnum.SECURE.toString());
                roles.add(UserTypeEnum.ADMIN.toString());
                break;
            case 2:
                roles.add(UserTypeEnum.USER.toString());
            case 3:
                roles.add(UserTypeEnum.USER.toString());
                roles.add(UserTypeEnum.SECURE.toString());
            default:
                roles.add(UserTypeEnum.GUEST.toString());
        }
        return roles;
    }

    /**
     * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
     *
     * @param roles {@link String} of roles
     * @return list of granted authorities
     */
    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

}
