package com.acktos.bluclient.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 8/6/15.
 */
public class Rate {

    public String id;
    public String name;
    public String amount;




    public static final String KEY_ID="id";
    public static final String KEY_NAME="name";
    public static final String KEY_AMOUNT="price";

    public Rate(String id, String name,String amount){

        this.id=id;
        this.name=name;
        this.amount=amount;

    }

    public Rate (JSONObject jsonObject){

        try{
            if(jsonObject.has(KEY_ID)) {
                this.id = jsonObject.getString(KEY_ID);
            }
            if(jsonObject.has(KEY_NAME)) {
                this.name = jsonObject.getString(KEY_NAME);
            }
            if(jsonObject.has(KEY_AMOUNT)) {
                this.amount= jsonObject.getString(KEY_AMOUNT);
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
            if(amount!=null){
                jsonObject.put(KEY_AMOUNT,amount);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
