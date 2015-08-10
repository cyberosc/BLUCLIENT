package com.acktos.bluclient.presentation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.acktos.bluclient.R;
import com.acktos.bluclient.models.RequestService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestDetailActivity extends AppCompatActivity {


    private TextView txtDriverName;
    private TextView txtDriverPlate;
    private CircleImageView imgDriverPhoto;
    private TextView txtZone;
    private TextView txtpickupAddress;
    private TextView txtarrivalAddress;
    private TextView txtPaymentMethod;
    private TextView txtAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);


        txtZone=(TextView) findViewById(R.id.lbl_zone);
        txtpickupAddress=(TextView) findViewById(R.id.lbl_pickup_address);
        txtarrivalAddress=(TextView) findViewById(R.id.lbl_arrival_address);
        txtPaymentMethod=(TextView) findViewById(R.id.txt_payment_method);
        txtAmount=(TextView) findViewById(R.id.txt_amount);
        txtDriverName=(TextView) findViewById(R.id.txt_driver_name);
        txtDriverPlate=(TextView) findViewById(R.id.txt_driver_plate);
        imgDriverPhoto=(CircleImageView) findViewById(R.id.img_profile);


        Bundle extras=getIntent().getExtras();

        if(extras!=null){

            try{
                String requestServiceString=extras.getString(RequestService.KEY_REQUEST_SERVICE);
                JSONObject jsonObject=new JSONObject(requestServiceString);

                RequestService requestService=new RequestService(jsonObject);

                if(requestService.driverName!=null){
                    txtDriverName.setText(requestService.driverName);
                }
                if(requestService.driverPlate!=null){
                    txtDriverPlate.setText(getString(R.string.plate)+":"+requestService.driverPlate);
                }

                if(requestService.zone!=null){
                    txtZone.setText(requestService.zone);
                }
                if(requestService.pickupAddress!=null){
                    txtpickupAddress.setText(requestService.pickupAddress);
                }
                if(requestService.arrivalAddress!=null){
                    txtarrivalAddress.setText(requestService.arrivalAddress);
                }
                if(requestService.payment!=null){
                    txtPaymentMethod.setText(requestService.payment);
                }
                if(requestService.amount!=null){
                    txtAmount.setText(requestService.amount);
                }

                if(requestService.driverPhoto!=null){
                    Picasso.with(this)
                            .load(requestService.driverPhoto)
                            .placeholder(R.mipmap.ic_profile)
                            .error(R.mipmap.ic_profile)
                            .into(imgDriverPhoto);
                }




            }catch (JSONException e){
                e.printStackTrace();
            }


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
