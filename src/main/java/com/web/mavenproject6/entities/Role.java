/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.mavenproject6.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Aleks
 */
@Entity(name = "role")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private Users user;
    private Integer role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
    
    public String toJSON() throws JSONException {
        JSONObject r = new JSONObject();
        r
                .put("id", id)
                .put("role", role);               
        return r.toString();
    }
}