package com.acktos.bluclient.presentation.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acktos.bluclient.R;
import com.acktos.bluclient.models.RequestService;

import java.util.List;


/**
 * Created by Acktos on 7/15/15.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {


    private List<RequestService> requestServices;
    private static OnRecyclerViewClickListener onRecyclerViewClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView requestId;
        public TextView pickupAddress;
        public TextView arrivalAddress;
        public TextView state;


        public ViewHolder(View itemView) {

            super(itemView);
            requestId=(TextView)itemView.findViewById(R.id.lbl_request_id);
            state=(TextView)itemView.findViewById(R.id.lbl_request_state);
            pickupAddress=(TextView)itemView.findViewById(R.id.lbl_pickup_address);
            arrivalAddress=(TextView)itemView.findViewById(R.id.lbl_arrival_address);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onRecyclerViewClickListener.onRecyclerViewClick(view, this.getLayoutPosition());
        }
    }

    public RequestAdapter(List<RequestService> requestServices,OnRecyclerViewClickListener onRecyclerViewClick){

        this.requestServices=requestServices;
        this.onRecyclerViewClickListener=onRecyclerViewClick;
    }

    @Override
    public int getItemCount() {

        return requestServices.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_request, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.requestId.setText(requestServices.get(i).id);
        viewHolder.state.setText(requestServices.get(i).state);
        viewHolder.pickupAddress.setText(requestServices.get(i).pickupAddress);
        viewHolder.arrivalAddress.setText(requestServices.get(i).arrivalAddress);
    }



    public interface OnRecyclerViewClickListener
    {

        public void onRecyclerViewClick(View v, int position);
    }

}

