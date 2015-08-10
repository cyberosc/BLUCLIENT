package com.acktos.bluclient.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.acktos.bluclient.R;
import com.acktos.bluclient.controllers.BaseController;
import com.acktos.bluclient.controllers.RequestsController;
import com.acktos.bluclient.controllers.UsersController;
import com.acktos.bluclient.models.RequestService;
import com.acktos.bluclient.models.User;
import com.acktos.bluclient.presentation.adapters.RequestAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;


public class RequestListActivity extends AppCompatActivity implements RequestAdapter.OnRecyclerViewClickListener{

    //ATTRIBUTES
    private List<RequestService> requestServices;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //COMPONENTS
    private RequestsController requestsController;
    private UsersController usersController;


    //UI REFERENCES
    private RecyclerView recyclerRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);


        //Initialize attributes
        requestServices=new ArrayList<>();

        //Initialize componentes
        requestsController=new RequestsController();
        usersController=new UsersController(this);

        setupRecyclerView();

    }

    private void setupRecyclerView(){


        recyclerRequest = (RecyclerView) findViewById(R.id.recycler_request);
        recyclerRequest.setHasFixedSize(true);
        //recyclerRequest.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(this);
        recyclerRequest.setLayoutManager(layoutManager);

        recyclerAdapter = new RequestAdapter(requestServices,this);
        recyclerRequest.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onResume(){

        super.onResume();
        User user=usersController.getUser();
        Log.i(BaseController.TAG, user.toString());

        requestsController.getRequests(user.id, new Response.Listener<List<RequestService>>() {
            @Override
            public void onResponse(List<RequestService> responseRequest) {

                if (responseRequest != null) {

                    requestServices.clear();
                    requestServices.addAll(responseRequest);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_list, menu);
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

    @Override
    public void onRecyclerViewClick(View v, int position) {

        Intent i=new Intent(this,RequestDetailActivity.class);
        i.putExtra(RequestService.KEY_REQUEST_SERVICE,requestServices.get(position).toString());

        startActivity(i);
    }
}
