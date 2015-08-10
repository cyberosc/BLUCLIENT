package com.acktos.bluclient.presentation.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acktos.bluclient.R;
import com.acktos.bluclient.models.Rate;
import com.acktos.bluclient.models.RequestService;

import java.util.List;


/**
 * Created by Acktos on 7/15/15.
 */
public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.ViewHolder> {


    private List<Rate> rates;
    private static OnRecyclerViewClickListener onRecyclerViewClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtName;
        public TextView txtAmount;
        


        public ViewHolder(View itemView) {

            super(itemView);
            txtName=(TextView)itemView.findViewById(R.id.txt_rate_name);
            txtAmount=(TextView)itemView.findViewById(R.id.txt_rate_amount);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onRecyclerViewClickListener.onRecyclerViewClick(view, this.getLayoutPosition());
        }
    }

    public RatesAdapter(List<Rate> rates,OnRecyclerViewClickListener onRecyclerViewClick){

        this.rates=rates;
        this.onRecyclerViewClickListener=onRecyclerViewClick;
    }

    @Override
    public int getItemCount() {

        return rates.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rate, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.txtName.setText(rates.get(i).name);
        viewHolder.txtAmount.setText(rates.get(i).amount);

    }



    public interface OnRecyclerViewClickListener
    {

        public void onRecyclerViewClick(View v, int position);
    }

}

