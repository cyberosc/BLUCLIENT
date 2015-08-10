package com.acktos.bluclient.presentation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.acktos.bluclient.R;
import com.acktos.bluclient.controllers.BaseController;
import com.acktos.bluclient.controllers.RatesController;
import com.acktos.bluclient.controllers.RequestsController;
import com.acktos.bluclient.controllers.UsersController;
import com.acktos.bluclient.models.Rate;
import com.acktos.bluclient.models.RequestService;
import com.acktos.bluclient.models.User;
import com.acktos.bluclient.presentation.adapters.RatesAdapter;
import com.acktos.bluclient.presentation.adapters.RequestAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

public class RatesActivity extends AppCompatActivity implements RatesAdapter.OnRecyclerViewClickListener{


    //ATTRIBUTES
    private List<Rate> rates;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //COMPONENTS
    private RatesController ratesController;

    //UI REFERENCES
    private RecyclerView recyclerRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        //Initialize attributes
        rates=new ArrayList<>();

        //Initialize componentes
        ratesController=new RatesController();
        setupRecyclerView();
    }

    private void setupRecyclerView(){


        recyclerRates = (RecyclerView) findViewById(R.id.recycler_rates);
        recyclerRates.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerRates.setLayoutManager(layoutManager);

        recyclerAdapter = new RatesAdapter(rates,this);
        recyclerRates.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onResume(){

        super.onResume();

        ratesController.getRates(new Response.Listener<List<Rate>>() {
            @Override
            public void onResponse(List<Rate> responseRates) {

                if (responseRates != null) {

                    rates.clear();
                    rates.addAll(responseRates);
                    recyclerAdapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(BaseController.TAG, volleyError.toString());
            }
        });

    }


    @Override
    public void onRecyclerViewClick(View v, int position) {

    }
}
