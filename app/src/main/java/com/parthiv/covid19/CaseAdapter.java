package com.parthiv.covid19;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.ViewHolder>{
    private ArrayList<CovidCases> listdata;

    // RecyclerView recyclerView;
    public CaseAdapter(ArrayList<CovidCases> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CovidCases myListData = listdata.get(position);
        holder.txtCountry.setText(listdata.get(position).getCountry());
        holder.txtDeath.setText(String.valueOf(listdata.get(position).getTotalDeaths()));
        holder.txtRecovered.setText(String.valueOf(listdata.get(position).getTotalRecovered()));
        holder.txtTotalCases.setText(String.valueOf(listdata.get(position).getTotalConfirmed()));

//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public  TextView txtRecovered, txtDeath, txtTotalCases,txtCountry;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtRecovered = (TextView) itemView.findViewById(R.id.recovered);
            this.txtDeath = (TextView) itemView.findViewById(R.id.deaths);
            this.txtTotalCases = (TextView) itemView.findViewById(R.id.totalCases);
            this.txtCountry = (TextView) itemView.findViewById(R.id.country);
            //relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
