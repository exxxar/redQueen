/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.entities;

import java.io.Serializable;

import javax.persistence.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Aleks
 */
@Entity(name = "users")
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String password;
    private String login;

    @Email
    @Column(unique = true)
    private String email;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private personal person;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private SecurityCode securityCode;

    private boolean accountExpired;
    private boolean accountLocked;
    private boolean enabled;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SecurityCode getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(SecurityCode securityCode) {
        this.securityCode = securityCode;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Users() {
        this.password = "";
        this.login = "";
        this.email = "";
        this.securityCode = null;
        this.accountExpired = false;
        this.accountLocked = false;
        this.enabled = false;
        this.role = null;
    }

    public Users(String password, String login, String email, SecurityCode securityCode, boolean accountExpired, boolean accountLocked, boolean enabled, Role role) {

        this.password = password;
        this.login = login;
        this.email = email;
        this.securityCode = securityCode;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.enabled = enabled;
        this.role = role;
    }

    public personal getPerson() {
        return person;
    }

    public void setPerson(personal person) {
        this.person = person;
    }

    public String toJSON() throws JSONException {
        JSONObject usr = new JSONObject();
        usr
                .put("id", id)
                .put("login", login)
                .put("password", password)
                .put("email", email)
                .put("securityCode", securityCode.toJSON())
                .put("accountExpired", accountExpired)
                .put("accountLocked", accountLocked)
                .put("enabled", enabled)
                .put("role", role.toJSON());
        return usr.toString();
    }

}
