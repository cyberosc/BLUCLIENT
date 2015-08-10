package com.acktos.bluclient.presentation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.acktos.bluclient.R;
import com.acktos.bluclient.controllers.BaseController;
import com.acktos.bluclient.controllers.RequestsController;
import com.acktos.bluclient.controllers.UsersController;
import com.acktos.bluclient.models.Receipt;
import com.acktos.bluclient.models.RequestService;
import com.acktos.bluclient.models.User;
import com.android.volley.Response;
import com.android.volley.VolleyError;



public class NewRequestFormActivity extends AppCompatActivity {


    //UI references
    private Spinner spnrPayment;
    private Button btnRequestService;
    private EditText edtxPickupAddress;
    private EditText edtxArrivalAddress;
    private View mProgressView;
    private View mFormView;

    //Attributes
    private String lastAddress="";
    private String lastLocation="";

    //Components
    private RequestsController requestsController;
    private UsersController usersController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request_form);

        spnrPayment=(Spinner) findViewById(R.id.spnr_payment);
        btnRequestService=(Button) findViewById(R.id.btn_request_service);
        edtxPickupAddress=(EditText) findViewById(R.id.edtx_pickup_address);
        edtxArrivalAddress=(EditText) findViewById(R.id.edtx_arrival_address);
        mProgressView = findViewById(R.id.request_progress);
        mFormView = findViewById(R.id.request_form);

        //Initialize Components
        requestsController=new RequestsController();
        usersController=new UsersController(this);

        Bundle extras=getIntent().getExtras();

        if(extras!=null){
            lastAddress=extras.getString(MapPickupActivity.LAST_KNOW_ADDRESS);
            lastLocation=extras.getString(MapPickupActivity.LAST_KNOW_LOCATION);
        }

        if(lastAddress!=null){
            edtxPickupAddress.setText(lastAddress);
        }

        // Set hardcoded array data

        //Todo: replace with data server payments
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrPayment.setAdapter(adapter);

        btnRequestService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRequestService();
            }
        });
    }

    public void attemptRequestService(){

        String arrivalAddress=edtxArrivalAddress.getText().toString();

        User user=usersController.getUser();

        String[] array=getResources().getStringArray(R.array.payment_methods);


        RequestService requestService=new RequestService(
                null,user.id,lastAddress,lastLocation,arrivalAddress,"1","Punto Transnal BLU");

        Log.i(BaseController.TAG,requestService.toString());


        showProgress(true);

        requestsController.addRequest(requestService, new Response.Listener<Receipt>() {
            @Override
            public void onResponse(Receipt receipt) {

                Log.i(BaseController.TAG, "response add request:" + receipt);
                showProgress(false);
                if(receipt!=null){

                    Intent i=new Intent(NewRequestFormActivity.this,ReceiptActivity.class);
                    i.putExtra(Receipt.KEY_RECEIPT,receipt.toString());
                    startActivity(i);
                }else{
                    Snackbar.make(mFormView, R.string.error_add_request, Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showProgress(false);
                Snackbar.make(mFormView, R.string.error_network, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
