package com.acktos.bluclient.presentation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.acktos.bluclient.R;
import com.acktos.bluclient.controllers.BaseController;
import com.acktos.bluclient.controllers.UsersController;
import com.acktos.bluclient.models.User;
import com.acktos.bluclient.presentation.MapPickupActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;


public class SingoutActivity extends AppCompatActivity {


    // COMPONENTS
    private UsersController usersController = null;

    // UI references.
    private TextInputLayout inputName;
    private TextInputLayout inputLastName;
    private TextInputLayout inputEmail;
    private TextInputLayout inputPhone;
    private TextInputLayout inputCC;
    private TextInputLayout inputPswrd;
    private Button btnAddUser;

    private View mProgressView;
    private View mFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singout);

        //Initialize components
        usersController=new UsersController(this);

        // Set up the login form
        inputName = (TextInputLayout) findViewById(R.id.input_name);
        inputLastName=(TextInputLayout) findViewById(R.id.input_last_name);
        inputEmail=(TextInputLayout) findViewById(R.id.input_email);
        inputPhone=(TextInputLayout) findViewById(R.id.input_phone);
        inputCC=(TextInputLayout) findViewById(R.id.input_cc);
        inputPswrd=(TextInputLayout) findViewById(R.id.input_pswrd);
        mProgressView = findViewById(R.id.signout_progress);
        mFormView = findViewById(R.id.signout_form);


        Button btnAddUser = (Button) findViewById(R.id.btn_add_user);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAddUser();
            }
        });

    }

    public void attemptAddUser() {


        // Reset errors.
        inputName.setError(null);
        inputLastName.setError(null);
        inputEmail.setError(null);
        inputPhone.setError(null);
        inputCC.setError(null);
        inputPswrd.setError(null);

        // Store values at the time of the login attempt.
        String name = inputName.getEditText().getText().toString();
        String lastName = inputLastName.getEditText().getText().toString();
        String email = inputEmail.getEditText().getText().toString();
        String phone = inputPhone.getEditText().getText().toString();
        String CC = inputCC.getEditText().getText().toString();
        String pswrd = inputPswrd.getEditText().getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(name)) {
            inputName.setError(getString(R.string.error_field_required));
            focusView = inputName;
            cancel = true;

        } else if (TextUtils.isEmpty(lastName)) {
            inputLastName.setError(getString(R.string.error_field_required));
            focusView = inputLastName;
            cancel = true;

        } else if (!isValidEmail(email)) {
            inputEmail.setError(getString(R.string.error_invalid_email));
            focusView = inputEmail;
            cancel = true;

        } else if (TextUtils.isEmpty(phone)) {
            inputPhone.setError(getString(R.string.error_field_required));
            focusView = inputPhone;
            cancel = true;

        } else if (TextUtils.isEmpty(CC)) {
            inputCC.setError(getString(R.string.error_field_required));
            focusView = inputCC;
            cancel = true;

        } else if (TextUtils.isEmpty(pswrd) && isPasswordValid(pswrd) ) {
            inputPswrd.setError(getString(R.string.error_incorrect_password));
            focusView = inputPswrd;
            cancel = true;

        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            showProgress(true);


            User user=new User("0",name,lastName,email,CC,phone,"Android",pswrd);

            usersController.addUser(user,"invalid_register_id", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals(BaseController.SUCCESS_CODE)) {
                        launchMapPickupActivity();
                    } else {

                        inputName.requestFocus();
                        Snackbar.make(mFormView, R.string.error_network, Snackbar.LENGTH_LONG).show();
                    }
                    showProgress(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    Log.i(BaseController.TAG, "entry to onErrorResponse from Activity");

                    showProgress(false);
                    Snackbar.make(mFormView, R.string.error_network, Snackbar.LENGTH_LONG).show();
                }
            });

        }
    }

    public final static boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
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

    private void launchMapPickupActivity(){

        Intent i=new Intent(this,MapPickupActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
