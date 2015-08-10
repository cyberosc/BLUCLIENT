package com.acktos.bluclient.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 8/6/15.
 */
public class Receipt {

    public String id;
    public String registerDate;



    public static final String KEY_ID="id";
    public static final String KEY_REGISTER_DATE="register_date";
    public static final String KEY_RECEIPT="receipt";

    public Receipt(String id, String registerDate){

        this.id=id;
        this.registerDate=registerDate;
    }

    public Receipt (JSONObject jsonObject){

        try{
            if(jsonObject.has(KEY_ID)) {
                this.id = jsonObject.getString(KEY_ID);
            }
            if(jsonObject.has(KEY_REGISTER_DATE)) {
                this.registerDate = jsonObject.getString(KEY_REGISTER_DATE);
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
            if(registerDate!=null){
                jsonObject.put(KEY_REGISTER_DATE,registerDate);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
