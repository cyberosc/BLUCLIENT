package com.acktos.bluclient.controllers;

import android.util.Log;

import com.acktos.bluclient.android.AppController;
import com.acktos.bluclient.android.Encrypt;
import com.acktos.bluclient.models.Receipt;
import com.acktos.bluclient.models.RequestService;
import com.acktos.bluclient.models.User;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Acktos on 7/27/15.
 */
public class RequestsController extends BaseController{

    public final static String TAG_ADD_REQUEST_SERVICE="ADD_REQUEST_SERVICE";
    public final static String TAG_GET_REQUESTS="GET_REQUESTS";

    public String addRequest(

            final RequestService request,
            final Response.Listener<Receipt> responseListener,
            final Response.ErrorListener errorListener){


        StringRequest jsonObjReq = new StringRequest(

                Request.Method.POST,
                API.ADD_REQUEST.getUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        Receipt receipt=null;
                        String code= getCodeRequest(response);

                        if(code!=null) {
                            if (code.equals(SUCCESS_CODE)) {

                                JSONObject jsonFields=getFieldsObject(response);
                                if(jsonFields!=null){
                                    receipt= new Receipt(jsonFields);
                                }

                                responseListener.onResponse(receipt);
                            } else {
                                responseListener.onResponse(receipt);
                            }
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        errorListener.onErrorResponse(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(RequestService.KEY_USER_ID, request.userId);
                params.put(RequestService.KEY_PICKUP_ADDRESS, request.pickupAddress);
                params.put(RequestService.KEY_PICKUP_LOCATION, request.pickupLocation);
                params.put(RequestService.KEY_PAYMENT, request.payment);
                params.put(RequestService.KEY_PAYMENT_METHOD, request.paymentMethod);
                params.put(RequestService.KEY_ARRIVAL_ADDRESS, request.arrivalAddress);


                params.put(User.KEY_ENCRYPT, Encrypt.md5(
                        request.userId + request.pickupAddress + request.pickupLocation + request.payment + request.paymentMethod+ request.arrivalAddress + BaseController.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_ADD_REQUEST_SERVICE);
        return null;
    }

    public void getRequests(
            final String userId,
            final Response.Listener<List<RequestService>> responseListener,
            final Response.ErrorListener errorListener) {


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                API.GET_REQUEST.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());
                        List<RequestService> requestServices;
                        String code = getCodeRequest(response);
                        if (code != null) {
                            if (code.equals(SUCCESS_CODE)) {

                                requestServices = new ArrayList<>();
                                JSONArray jsonArrayRequests = getFieldsArray(response);

                                if (jsonArrayRequests != null) {

                                    for (int i = 0; i < jsonArrayRequests.length(); i++) {

                                        try {
                                            JSONObject requestObject = jsonArrayRequests.getJSONObject(i);

                                            RequestService request=new RequestService(requestObject);
                                            requestServices.add(request);

                                            Log.i(TAG,"request:"+request.toString());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    responseListener.onResponse(requestServices);
                                }

                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                errorListener.onErrorResponse(volleyError);
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(RequestService.KEY_ID, userId);
                params.put(User.KEY_ENCRYPT, Encrypt.md5(userId + BaseController.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_GET_REQUESTS);

    }
}
