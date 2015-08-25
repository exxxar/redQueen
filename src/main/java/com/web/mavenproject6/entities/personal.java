/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Алексей
 */
@Entity
@Table(name = "personal")
public class personal implements Serializable {

    @OneToMany(mappedBy = "personal_guest",
            cascade = CascadeType.ALL)
    private List<guest> guests;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long personal_id;
    @Column(unique = true)
    private String accessNumber;

    @NotNull
    @Length(max = 45)
    private String tname;
    @NotNull
    @Length(max = 45)
    private String sname;
    @NotNull
    @Length(max = 45)
    private String fname;

    private String phone;//номер телефона    
    private String addres;//место жительства
    private String post;//должность
    private String office;//номер офиса
    private String stage;//этаж

    @NotNull
    private String passportSeria;

    @NotNull
    private String passportNum;

    private byte[] photo;

    private boolean isActive;

    private Date lastUpdate;

    @Length(max = 255)
    private String info;

    @OneToOne
    private Users user;

    public List<guest> getGuests() {
        return guests;
    }

    public void setGuests(List<guest> guests) {
        this.guests = guests;
    }

    public long getPersonal_id() {
        return personal_id;
    }

    public void setPersonal_id(long personal_id) {
        this.personal_id = personal_id;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber) {
        this.accessNumber = accessNumber;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public personal() {
        this.accessNumber = "";
        this.tname = "";
        this.sname = "";
        this.fname = "";
        this.phone = "";
        this.addres = "";
        this.post = "";
        this.office = "";
        this.stage = "";
        this.passportSeria = "";
        this.passportNum = "";
        this.photo = new byte[1];
        this.isActive = false;
        this.info = "";
        this.user = null;
        this.lastUpdate = new Date();
    }

    public personal(String accessNumber, String tname, String sname, String fname, String phone, String addres, String post, String office, String stage, String passportSeria, String passportNum, byte[] photo, boolean isActive, String info, Users user) {
        this.accessNumber = accessNumber;
        this.tname = tname;
        this.sname = sname;
        this.fname = fname;
        this.phone = phone;
        this.addres = addres;
        this.post = post;
        this.office = office;
        this.stage = stage;
        this.passportSeria = passportSeria;
        this.passportNum = passportNum;
        this.photo = photo;
        this.isActive = isActive;
        this.info = info;
        this.user = user;
        this.lastUpdate = new Date();
    }
    
  
    @Override
    public String toString(){
        JSONObject person = new JSONObject();
        try {       
        person
                .put("personal_id", personal_id)
                .put("accessNumber", accessNumber)
                .put("fname", fname)
                .put("fname", fname)
                .put("sname", sname)
                .put("tname", tname)
                .put("phone", phone)
                .put("addres", addres)
                .put("post", post)
                .put("office", office)
                .put("stage", stage)
                .put("passportSeria", passportSeria)
                .put("passportNum", passportNum)
                //.put("photo", photo.length > 1 ? "exist" : "not exist")
                .put("isActive", isActive)
                .put("info", info)
                //.put("user", user.toJSON())
                .put("lastUpdate", lastUpdate);
//
//        if (getGuests() != null) {
//            JSONArray ar = new JSONArray();
//            for (guest g : getGuests()) {
//                ar.put(g.toJSON());
//            }
//            person.put("guests", ar);
//        }
          return (new JSONObject()).put("personal", person).toString();
        }
        catch(Exception e){
            
        }
        return "";
    }

}
