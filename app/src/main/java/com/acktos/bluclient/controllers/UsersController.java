package com.acktos.bluclient.controllers;

import android.content.Context;
import android.util.Log;

import com.acktos.bluclient.android.AppController;
import com.acktos.bluclient.android.Encrypt;
import com.acktos.bluclient.android.InternalStorage;
import com.acktos.bluclient.models.User;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acktos on 7/27/15.
 */

public class UsersController extends BaseController{

    private Context context;
    private InternalStorage internalStorage;
    public final static String TAG_USER_LOGIN_REQUEST="LOGIN_REQUEST";
    public final static String TAG_ADD_USER_REQUEST="ADD_REQUEST";
    public final static String FILE_USER_PROFILE="com.acktos.blu.USER_PROFILE";


    public UsersController(Context context){
        this.context=context;
        internalStorage=new InternalStorage(this.context);
    }

    public String userLogin(
            final String email,
            final String password,
            final Response.Listener<String> responseListener,
            final Response.ErrorListener errorListener){


        StringRequest jsonObjReq = new StringRequest(

                Request.Method.POST,
                API.USER_LOGIN.getUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        String code= getCodeRequest(response);

                        if(code!=null) {
                            if (code.equals(SUCCESS_CODE)) {

                                JSONObject jsonFields=getFieldsObject(response);
                                User driver= new User(jsonFields);
                                saveUserProfile(driver);
                                responseListener.onResponse(SUCCESS_CODE);
                            } else {
                                responseListener.onResponse(FAILED_CODE);
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

                params.put(User.KEY_EMAIL, email);
                params.put(User.KEY_PASSWORD, password);
                params.put(User.KEY_ENCRYPT, Encrypt.md5(email + password + BaseController.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_USER_LOGIN_REQUEST);
        return null;
    }


    public String addUser(

            final User user,
            final String registerId,
            final Response.Listener<String> responseListener,
            final Response.ErrorListener errorListener){


        StringRequest jsonObjReq = new StringRequest(

                Request.Method.POST,
                API.ADD_USER.getUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        String code= getCodeRequest(response);

                        if(code!=null) {
                            if (code.equals(SUCCESS_CODE)) {

                                JSONObject jsonFields=getFieldsObject(response);
                                User user= new User(jsonFields);
                                saveUserProfile(user);
                                responseListener.onResponse(SUCCESS_CODE);
                            } else {
                                responseListener.onResponse(FAILED_CODE);
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

                params.put(User.KEY_NAME, user.name);
                params.put(User.KEY_LAST_NAME, user.lastName);
                params.put(User.KEY_CC, user.cc);
                params.put(User.KEY_EMAIL, user.email);
                params.put(User.KEY_PHONE, user.phone);
                params.put(User.KEY_PASSWORD, user.pswrd);
                params.put(User.KEY_USER_AGENT, user.userAgent);
                params.put(User.KEY_REGISTER_ID, registerId);
                params.put(User.KEY_ENCRYPT, Encrypt.md5(
                                user.name+user.lastName+user.email+user.pswrd+user.phone+user.cc+user.userAgent+registerId
                                +BaseController.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_ADD_USER_REQUEST);
        return null;
    }


    /**
     * save profile into internal storage
     */
    private void saveUserProfile(User user){

        internalStorage.saveFile(FILE_USER_PROFILE, user.toString());
    }


    public boolean profileExists(){

        boolean exists=false;

        User driver=getUser();

        if(driver!=null){
            exists=true;
        }
        return exists;
    }

    public User getUser(){

        User user=null;

        if(internalStorage.isFileExists(FILE_USER_PROFILE)){

            try {
                String profileString=internalStorage.readFile(FILE_USER_PROFILE);
                JSONObject jsonObject=new JSONObject(profileString);

                user=new User(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return user;
    }


    public String saveRegisterId(

            final String registerId,
            final Response.Listener<String> responseListener,
            final Response.ErrorListener errorListener){


        StringRequest jsonObjReq = new StringRequest(

                Request.Method.POST,
                API.SAVE_REGISTER_ID.getUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        String code= getCodeRequest(response);

                        if(code!=null) {
                            if (code.equals(SUCCESS_CODE)) {

                                Log.i(TAG,"Register id saved successful");
                                responseListener.onResponse(SUCCESS_CODE);
                            } else {
                                Log.i(TAG,"Register is NOT saved");
                                responseListener.onResponse(FAILED_CODE);
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

                User user=getUser();

                Map<String, String> params = new HashMap<>();

                params.put(User.KEY_ID, user.id);
                params.put(User.KEY_REGISTER_ID, registerId);
                params.put(User.KEY_USER_AGENT, "Android");
                params.put(User.KEY_ENCRYPT, Encrypt.md5(user.id + registerId +"Android" +BaseController.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_USER_LOGIN_REQUEST);
        return null;
    }
}
