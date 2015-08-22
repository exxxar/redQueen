/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.entities;

/**
 *
 * @author Aleks
 */
import com.web.mavenproject6.utility.TypeActivationEnum;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.json.JSONException;
import org.json.JSONObject;

@Entity(name = "security_code")
public class SecurityCode implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Users user;

    private String code;
    private Date timeRequest;
    private TypeActivationEnum typeActivationEnum;

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

    public String getCode() {
        return code;
    }

    public void setCode(String secureString) {
        this.code = secureString;
    }

    public Date getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(Date timeRequest) {
        this.timeRequest = timeRequest;
    }

    public TypeActivationEnum getTypeActivationEnum() {
        return typeActivationEnum;
    }

    public void setTypeActivationEnum(TypeActivationEnum typeActivationEnum) {
        this.typeActivationEnum = typeActivationEnum;
    }

    public String toJSON() throws JSONException {
        JSONObject sec = new JSONObject();
        sec
                .put("id", id)
                .put("code", code)
                .put("timeRequest", timeRequest)
                .put("typeActivationEnum", typeActivationEnum);
        return sec.toString();
    }
}
