/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Алексей
 */
@Entity
@Table(name = "guest")
public class guest implements Serializable {

    @ManyToOne
    @JoinColumn(name = "personal_id")
    private personal personal_guest;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Length(max = 45)
    private String fname;
    private String sname;
    private String tname;
   
    @NotNull
    private String passportSeria;
    
    @NotNull
    private String passportNum;
    private boolean state;
    private byte[] photo;
    
    public personal getPersonal_guest() {
        return personal_guest;
    }

    public void setPersonal_guest(personal personal_guest) {
        this.personal_guest = personal_guest;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getPassportSeria() {
        return passportSeria;
    }

    public void setPassportSeria(String passportSeria) {
        this.passportSeria = passportSeria;
    }

    public String getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(String passportNum) {
        this.passportNum = passportNum;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public guest() {
         this.fname = "";
        this.sname = "";
        this.tname = "";
        this.passportSeria = "";
        this.passportNum = "";
        this.state = false;
        this.photo = new byte[1];
    }

    public guest(String fname, String sname, String tname, String passportSeria, String passportNum, boolean state, byte[] photo) {
        this.fname = fname;
        this.sname = sname;
        this.tname = tname;
        this.passportSeria = passportSeria;
        this.passportNum = passportNum;
        this.state = state;
        this.photo = photo;
    }
  
    
 public String toJSON() throws JSONException {
        JSONObject g = new JSONObject();
        g
                .put("id", id)
                .put("fname", fname)
                .put("sname", fname)
                .put("tname", fname)
                .put("passportSeria", fname)
                .put("passportNum", fname)
                .put("state", fname)
                .put("photo", photo.length > 1 ? "exist" : "not exist");               
        return g.toString();
    }    

}
