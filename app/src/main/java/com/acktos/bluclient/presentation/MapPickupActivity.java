package com.acktos.bluclient.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.acktos.bluclient.R;
import com.acktos.bluclient.controllers.BaseController;
import com.acktos.bluclient.controllers.UsersController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapPickupActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnCameraChangeListener{


    //Google API client.
    private static final String KEY_IN_RESOLUTION = "is_in_resolution";
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsInResolution;

    //Google map
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    //UI References
    Button btnPickupHere;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    //Attributes
    private String lastAddress;
    private LatLng lastLocation;

    //Components
    private UsersController usersController;

    //Constants
    public static final String LAST_KNOW_ADDRESS="LAST_ADDRESS";
    public static final String LAST_KNOW_LOCATION="LAST_LOCATION";


    //google register id
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String SENDER_ID = "331183627800";

    String regid;
    GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_pickup);

        //Initialize components
        usersController=new UsersController(this);

        //Navigation View
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_hamburger_70);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                Intent i;

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    case R.id.drawer_new_request:
                        return true;

                    case R.id.drawer_my_request:
                        i=new Intent(MapPickupActivity.this,RequestListActivity.class);
                        startActivity(i);
                        return true;

                    case R.id.drawer_rates:
                        i=new Intent(MapPickupActivity.this,RatesActivity.class);
                        startActivity(i);
                        return true;

                    case R.id.drawer_logout:


                        deleteFile(UsersController.FILE_USER_PROFILE);
                        i=new Intent(MapPickupActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                        Log.i(BaseController.TAG,"success logout");

                        return true;

                    default:

                        return true;


                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        //Initialize UI
        btnPickupHere=(Button) findViewById(R.id.btn_pickup_here);
        btnPickupHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptPickupHere();
            }
        });

        setUpMapIfNeeded();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(this);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(BaseController.TAG, "No valid Google Play Services APK found.");
        }
    }


    @Override
    protected void onStart() {

        super.onStart();

        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        setUpMapIfNeeded();

    }

    @Override
    protected  void onStop(){


        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public  void attemptPickupHere(){

        if(lastLocation!=null){

            Intent i=new Intent(this, NewRequestFormActivity.class);
            if(lastAddress!=null){

                i.putExtra(LAST_KNOW_ADDRESS,lastAddress);

            }else{

                i.putExtra(LAST_KNOW_ADDRESS,"");
                Toast.makeText(MapPickupActivity.this,getString(R.string.error_geocode_address),Toast.LENGTH_LONG).show();
            }

            i.putExtra(LAST_KNOW_LOCATION, lastLocation.latitude + "," + lastLocation.longitude);
            startActivity(i);

        }else{
            Toast.makeText(MapPickupActivity.this,getString(R.string.msg_waiting_location),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        Location lastKnowLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        moveCameraToLocation(lastKnowLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(BaseController.TAG, "GoogleApiClient connection suspended");
        retryConnecting();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i(BaseController.TAG, "GoogleApiClient connection failed: " + connectionResult.toString());
        if (!connectionResult.hasResolution()) {
            // Show a localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(
                    connectionResult.getErrorCode(), this, 0, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            retryConnecting();
                        }
                    }).show();
            return;
        }
        // If there is an existing resolution error being displayed or a resolution
        // activity has started before, do nothing and wait for resolution
        // progress to be completed.
        if (mIsInResolution) {
            return;
        }
        mIsInResolution = true;
        try {
            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(BaseController.TAG, "Exception while starting resolution activity", e);
            retryConnecting();
        }
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setOnCameraChangeListener(this);
    }

    private void retryConnecting() {
        mIsInResolution = false;
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }
    protected synchronized void buildGoogleApiClient() {

        Log.i(BaseController.TAG, "Entry to buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void moveCameraToLocation(Location location){

        float cameraZoom=mMap.getMaxZoomLevel()-4;
        LatLng point;

        if(location!=null){

            point=new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, cameraZoom));
        }

    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        if(cameraPosition.target!=null){
            getAddress(cameraPosition.target);
            lastLocation=cameraPosition.target;
        }

    }

    private void getAddress(LatLng latlng){

        GetAddressTask getAddressTask=new GetAddressTask(this);
        getAddressTask.execute(latlng);

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(BaseController.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        //Persists the registration ID in shared preferences.
        return getSharedPreferences(MapPickupActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * If result is empty, the app needs to register.
     * @return registration ID, or empty string if there is no existing registration ID.
     */
    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        final String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.isEmpty()) {
            Log.i(BaseController.TAG, "Registration not found.");
            return "";
        }else{
            Log.i(BaseController.TAG,"Registration id:"+registrationId);
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(BaseController.TAG, "App version changed.");
            return "";
        }else{
            new AsyncTask<Void,Void,Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    sendRegistrationIdToBackend(registrationId);
                    return null;
                }

            }.execute(null, null, null);
        }
        return registrationId;
    }
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void,Void,String>() {

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(MapPickupActivity.this);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // save registerId in backend
                    sendRegistrationIdToBackend(regid);

                    // save regiterId in shared preferences
                    storeRegistrationId(MapPickupActivity.this, regid);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(BaseController.TAG,msg);
            }

        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String registerId) {


        usersController.saveRegisterId(registerId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(BaseController.TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private class GetAddressTask extends AsyncTask<LatLng,Void,String> {

        private Context context;
        private int MAX_RESULTS=1;

        public GetAddressTask(Context context) {
            super();
            this.context=context;
        }

        @Override
        protected String doInBackground(LatLng... args) {

            String addressText=null;
            List<Address> addresses=null;
            Geocoder geocoder=new Geocoder(context, Locale.getDefault());
            LatLng latlng=args[0];

            try {
                addresses=geocoder.getFromLocation(latlng.latitude, latlng.longitude, MAX_RESULTS);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(BaseController.TAG, "IO Exception in getFromLocation()");
                return addressText;
            }catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +Double.toString(latlng.latitude) +" , " +Double.toString(latlng.longitude) +" passed to address service";
                Log.e(BaseController.TAG, errorString);
                e2.printStackTrace();
                //return errorString;
                return addressText;
            }

            if(isCancelled()){
                return addressText;
                //return "Task cancelled";
            }

            if(addresses!=null && addresses.size()>0){
                Address address=addresses.get(0);
                addressText = String.format("%s",address.getMaxAddressLineIndex() > 0 ?address.getAddressLine(0) : "");

            }
            return addressText;
        }

        @Override
        protected void onCancelled() {
            Log.e(BaseController.TAG,"task cancelled");
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            try{
                Log.i(BaseController.TAG,"address:"+address);
            }catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }

            lastAddress=address;
        }

        @Override
        protected void onPreExecute() {
            //mProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }


    }
}
