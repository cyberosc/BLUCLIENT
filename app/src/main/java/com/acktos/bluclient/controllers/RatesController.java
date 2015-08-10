package com.acktos.bluclient.controllers;

import android.util.Log;

import com.acktos.bluclient.android.AppController;
import com.acktos.bluclient.android.Encrypt;
import com.acktos.bluclient.models.Rate;
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
 * Created by Acktos on 8/6/15.
 */
public class RatesController extends BaseController{


    public final static String TAG_GET_RATES="GET_RATES";

    public void getRates(
            final Response.Listener<List<Rate>> responseListener,
            final Response.ErrorListener errorListener) {


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                BaseController.API.GET_RATES.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "rates response:"+response.toString());
                        List<Rate> rates;
                        String code = getCodeRequest(response);


                        if (code != null) {
                            if (code.equals(SUCCESS_CODE)) {

                                rates = new ArrayList<>();
                                JSONArray jsonArrayRequests = getFieldsArray(response);

                                if (jsonArrayRequests != null) {

                                    for (int i = 0; i < jsonArrayRequests.length(); i++) {

                                        try {
                                            JSONObject requestObject = jsonArrayRequests.getJSONObject(i);

                                            Rate rate=new Rate(requestObject);
                                            rates.add(rate);

                                            Log.i(TAG,"rate:"+rate.toString());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    responseListener.onResponse(rates);
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

                params.put(User.KEY_ENCRYPT, Encrypt.md5(BaseController.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_GET_RATES);

    }
}
