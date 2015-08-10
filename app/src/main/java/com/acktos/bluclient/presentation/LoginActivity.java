package com.acktos.bluclient.presentation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.TextInputLayout;

import com.acktos.bluclient.controllers.BaseController;
import com.acktos.bluclient.controllers.UsersController;
import com.acktos.bluclient.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // COMPONENTS
    private UsersController usersController = null;

    // UI references.
    private TextInputLayout inplayoutEmail;
    private TextInputLayout inplayoutPassword;

    private EditText edtxPassword;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize components
        usersController=new UsersController(this);

        // check if driver is loggued already
        isUserLogged();

        // Set up the login form.
        edtxPassword = (EditText) findViewById(R.id.edtx_password);
        inplayoutEmail=(TextInputLayout) findViewById(R.id.inplayot_email);
        inplayoutPassword=(TextInputLayout) findViewById(R.id.inplayot_password);

        edtxPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button btnRegister =(Button)findViewById(R.id.email_sign_up_button);
        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSingUp();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    private void launchSingUp(){

        Intent i=new Intent(this,SingoutActivity.class);
        startActivity(i);
    }

    private void isUserLogged(){

        if(usersController.profileExists()){
            launchMapickupActivity();
        }
    }


    public void attemptLogin() {


        // Reset errors.
        inplayoutEmail.setError(null);
        inplayoutPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = inplayoutEmail.getEditText().getText().toString();
        String password = inplayoutPassword.getEditText().getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            inplayoutPassword.setError(getString(R.string.error_invalid_password));
            focusView = inplayoutPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            inplayoutEmail.setError(getString(R.string.error_field_required));
            focusView = inplayoutEmail;
            cancel = true;
        } else if (!isValidEmail(email)) {
            inplayoutEmail.setError(getString(R.string.error_invalid_email));
            focusView = inplayoutEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            showProgress(true);

            usersController.userLogin(email, password, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals(BaseController.SUCCESS_CODE)) {
                        launchMapickupActivity();
                    } else {
                        inplayoutPassword.setError(getString(R.string.error_incorrect_password));
                        inplayoutPassword.requestFocus();
                    }
                    showProgress(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    Log.i(BaseController.TAG, "entry to onErrorResponse from Activity");

                    showProgress(false);
                    Snackbar.make(mLoginFormView, R.string.error_network, Snackbar.LENGTH_LONG).show();
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
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void launchMapickupActivity(){

        Intent i=new Intent(this,MapPickupActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



}

