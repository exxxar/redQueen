/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.web.mavenproject6.other;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.web.mavenproject6.service.GuestService;
import com.web.mavenproject6.service.PersonalService;

//@Component
public class UserInfoManager 
{	
	@Autowired
	PersonalService ps;
        
	@Autowired
	GuestService gs;
	
	public UserInfoManager()
	{
		
	}
	
	//new UserInfoManager.getInfo();
	public JSONObject getInfo(JSONObject _jso) throws JSONException
	{
            
//                
//		String table;
//		if (!_jso.has("Table"))
//		{
//			return new JSONObject().put("Fail", "Miss param \"Table\"");
//		}
//		table = _jso.getString("Table");
//		if (!table.equals("Personal") && !table.equals("Guest"))
//		{
//			return new JSONObject().put("Fail", "Incorret table name");
//		}
//		//findOne(ID)
//		if (_jso.has("ID"))
//		{
//			long id = _jso.getLong("ID");
//			if (table.equals("Personal"))
//			{
//				return ps.findOne(id).toJSON();
//			}
//			else
//			{
//				return gs.findOne(id).toJSON();
//			}
//		}
//		//findSNP(Surname,Name,Patronymic)
//		if (_jso.has("Surname") && _jso.has("Name") && _jso.has("Patronymic"))
//		{
//			String surname = _jso.getString("Surname");
//			String name = _jso.getString("Name");
//			String patronymic = _jso.getString("Patronymic");
//			
//			if (table.equals("Personal"))
//			{
//				return ps.findBySNP(surname,name,patronymic).toJSON();
//			}
//			else
//			{
//				return gs.findBySNP(surname,name,patronymic).toJSON();
//			}
//		}
//		else
//		{
//			return new JSONObject().put("Fail", "Missing params");
//		}
            return new JSONObject();
	}
}
