package com.acktos.bluclient.presentation;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acktos.bluclient.R;
import com.acktos.bluclient.models.Receipt;

import org.json.JSONException;
import org.json.JSONObject;


public class ReceiptActivity extends AppCompatActivity {


    //UI References
    private TextView txtReceiptId;
    private TextView txtServiceHour;
    private Button btnAcceptReceipt;

    //Attributes
    private Receipt receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        //Initialize ui references
        txtReceiptId=(TextView) findViewById(R.id.txt_receipt_id);
        txtServiceHour=(TextView) findViewById(R.id.txt_service_hour);
        btnAcceptReceipt=(Button) findViewById(R.id.btn_accept_receipt);

        // Get extras from previous activity
        Bundle extras=getIntent().getExtras();

        if(extras!=null){

            try {
            String receiptString=extras.getString(Receipt.KEY_RECEIPT);

                JSONObject jsonObject=new JSONObject(receiptString);
                receipt=new Receipt(jsonObject);

                txtReceiptId.setText(receipt.id);
                txtServiceHour.setText(receipt.registerDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        btnAcceptReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ReceiptActivity.this,MapPickupActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receipt, menu);
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
