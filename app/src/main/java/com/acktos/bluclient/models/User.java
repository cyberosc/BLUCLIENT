package com.acktos.bluclient.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 7/13/15.
 */
public class User {

    public String id;
    public String name;
    public String lastName;
    public String email;
    public String cc;
    public String phone;
    public String userAgent;
    public String pswrd;

    public static final String KEY_ID="id";
    public static final String KEY_NAME="name";
    public static final String KEY_LAST_NAME="last_name";
    public static final String KEY_CC="cc";
    public static final String KEY_PHONE="phone";
    public static final String KEY_REGISTER_ID="mobile_id";
    public static final String KEY_USER_AGENT="user_agent";
    public static final String KEY_EMAIL="email";
    public static final String KEY_PASSWORD="pswrd";
    public static final String KEY_ENCRYPT="encrypt";

    public User(String id, String name,String lastName,String email,String cc,String phone,String userAgent,String pswrd){

        this.id=id;
        this.name=name;
        this.lastName=lastName;
        this.email=email;
        this.cc=cc;
        this.phone=phone;
        this.userAgent=userAgent;
        this.pswrd=pswrd;
    }

    public User (JSONObject jsonObject){

        try{
            if(jsonObject.has(KEY_ID)) {
                this.id = jsonObject.getString(KEY_ID);
            }
            if(jsonObject.has(KEY_NAME)) {
                this.name = jsonObject.getString(KEY_NAME);
            }
            if(jsonObject.has(KEY_LAST_NAME)) {
                this.lastName = jsonObject.getString(KEY_LAST_NAME);
            }
            if(jsonObject.has(KEY_CC)) {
                this.cc = jsonObject.getString(KEY_CC);
            }
            if(jsonObject.has(KEY_EMAIL)) {
                this.email = jsonObject.getString(KEY_EMAIL);
            }
            if(jsonObject.has(KEY_PHONE)) {
                this.phone = jsonObject.getString(KEY_PHONE);
            }
            if(jsonObject.has(KEY_USER_AGENT)) {
                this.userAgent = jsonObject.getString(KEY_USER_AGENT);
            }
            if(jsonObject.has(KEY_PASSWORD)) {
                this.pswrd = jsonObject.getString(KEY_PASSWORD);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public String toString(){

        JSONObject jsonObject=new JSONObject();

        try{
            if(id!=null){
                jsonObject.put(KEY_ID,id);
            }
            if(name!=null){
                jsonObject.put(KEY_NAME,name);
            }
            if(lastName!=null){
                jsonObject.put(KEY_LAST_NAME,lastName);
            }
            if(cc!=null){
                jsonObject.put(KEY_CC,cc);
            }
            if(email!=null){
                jsonObject.put(KEY_EMAIL,email);
            }
            if(phone!=null){
                jsonObject.put(KEY_PHONE,phone);
            }
            if(userAgent!=null){
                jsonObject.put(KEY_USER_AGENT,userAgent);
            }
            if(pswrd!=null){
                jsonObject.put(KEY_PASSWORD,pswrd);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
