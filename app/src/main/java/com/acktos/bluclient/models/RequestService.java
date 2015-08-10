package com.acktos.bluclient.models;

import android.util.Log;

import com.acktos.bluclient.controllers.BaseController;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 7/27/15.
 */
public class RequestService {

    public String id;
    public String userId;
    public String pickupAddress;
    public String pickupLocation;
    public String arrivalAddress;
    public String payment;
    public String paymentMethod;
    public String driverName;
    public String driverPhoto;
    public String driverPlate;
    public String zone;
    public String amount;
    public String state;
    public String brand;

    public static final String KEY_ID="id";
    public static final String KEY_USER_ID="client";
    public static final String KEY_PICKUP_ADDRESS="place";
    public static final String KEY_PICKUP_LOCATION="coordinates";
    public static final String KEY_ARRIVAL_ADDRESS="destination";
    public static final String KEY_PAYMENT="payment";
    public static final String KEY_PAYMENT_METHOD="payment_dialog";
    public static final String KEY_DRIVER_NAME="driver";
    public static final String KEY_DRIVER_PLATE="plate";
    public static final String KEY_DRIVER_PHOTO="driver_image";
    public static final String KEY_AMOUNT="rate_value";
    public static final String KEY_STATE="state_name";
    public static final String KEY_ZONE="rate";
    public static final String KEY_BRAND="brand";

    public static final String KEY_REQUEST_SERVICE="request_service";


    public RequestService(
            String id, String userId, String pickupAddress,
            String pickupLocation, String arrivalAddress, String payment, String paymentMethod){

        this.id=id;
        this.userId=userId;
        this.pickupAddress=pickupAddress;
        this.pickupLocation=pickupLocation;
        this.arrivalAddress=arrivalAddress;
        this.payment=payment;
        this.paymentMethod=paymentMethod;

    }

    public RequestService(JSONObject jsonObject){

        try{
            if(jsonObject.has(KEY_ID)) {

                this.id = jsonObject.getString(KEY_ID);
            }
            if(jsonObject.has(KEY_USER_ID)) {

                this.userId = jsonObject.getString(KEY_USER_ID);
            }
            if(jsonObject.has(KEY_PICKUP_ADDRESS)) {

                this.pickupAddress = jsonObject.getString(KEY_PICKUP_ADDRESS);
            }
            if(jsonObject.has(KEY_PICKUP_LOCATION)) {

                this.pickupLocation = jsonObject.getString(KEY_PICKUP_LOCATION);
            }
            if(jsonObject.has(KEY_ARRIVAL_ADDRESS)) {

                this.arrivalAddress = jsonObject.getString(KEY_ARRIVAL_ADDRESS);
            }
            if(jsonObject.has(KEY_PAYMENT)) {

                this.payment = jsonObject.getString(KEY_PAYMENT);
            }
            if(jsonObject.has(KEY_DRIVER_NAME)) {

                this.driverName = jsonObject.getString(KEY_DRIVER_NAME);
            }
            if(jsonObject.has(KEY_DRIVER_PLATE)) {

                this.driverPlate = jsonObject.getString(KEY_DRIVER_PLATE);
            }

            if(jsonObject.has(KEY_DRIVER_PHOTO)) {

                this.driverPhoto = jsonObject.getString(KEY_DRIVER_PHOTO);
            }

            if(jsonObject.has(KEY_BRAND)) {

                this.brand = jsonObject.getString(KEY_BRAND);
            }

            if(jsonObject.has(KEY_ZONE)) {

                this.zone = jsonObject.getString(KEY_ZONE);
            }

            if(jsonObject.has(KEY_STATE)) {

                this.state = jsonObject.getString(KEY_STATE);
            }

            if(jsonObject.has(KEY_AMOUNT)) {

                this.amount = jsonObject.getString(KEY_AMOUNT);
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
            if(userId!=null){
                jsonObject.put(KEY_USER_ID,userId);
            }
            if(pickupAddress!=null){
                jsonObject.put(KEY_PICKUP_ADDRESS,pickupAddress);
            }
            if(pickupLocation!=null){
                jsonObject.put(KEY_PICKUP_LOCATION,pickupLocation);
            }
            if(arrivalAddress!=null){
                jsonObject.put(KEY_ARRIVAL_ADDRESS,arrivalAddress);
            }
            if(payment!=null){
                jsonObject.put(KEY_PAYMENT,payment);
            }
            if(paymentMethod!=null){
                jsonObject.put(KEY_PAYMENT_METHOD,paymentMethod);
            }

            if(driverName!=null){
                jsonObject.put(KEY_DRIVER_NAME,driverName);
            }
            if(driverPlate!=null){
                jsonObject.put(KEY_DRIVER_PLATE,driverPlate);
            }
            if(driverPhoto!=null){
                jsonObject.put(KEY_DRIVER_PHOTO,driverPhoto);
            }
            if(brand!=null){
                jsonObject.put(KEY_BRAND,brand);
            }
            if(zone!=null){
                jsonObject.put(KEY_ZONE,zone);
            }
            if(state!=null){
                jsonObject.put(KEY_STATE,state);
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
