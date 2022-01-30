package com.example.qrscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyViewHolder> {
    //Chosen firebase Context here
    Context context;

    ArrayList<Dates> listDates;

    public DateAdapter(Context context, ArrayList<Dates> list) {
        this.context = context;
        this.listDates = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.experiment,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Dates dates = listDates.get(position);
        holder.Name.setText("Name:  "+ dates.getName());
        holder.Id.setText("Id:  "+ dates.getId());
        holder.Sem.setText("Sem:  " +dates.getSem());
        holder.Branch.setText("Branch:  "+ dates.getBranch());
        holder.time.setText("Time:  " +dates.getTime());

    }

    @Override
    public int getItemCount() {
        return listDates.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Name,Id,Sem,Branch,time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.tvName);
            Id = itemView.findViewById(R.id.tvID);
            Sem = itemView.findViewById(R.id.tvSem);
            Branch = itemView.findViewById(R.id.tvBranch);
            time = itemView.findViewById(R.id.tvTime);

        }
    }
}
