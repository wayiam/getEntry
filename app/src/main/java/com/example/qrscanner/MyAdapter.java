package com.example.qrscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    //Chosen firebase Context here
    Context context;

    ArrayList<Visitors> list;

    public MyAdapter(Context context, ArrayList<Visitors> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Visitors visitors = list.get(position);
        holder.Name.setText(visitors.getName());
        holder.Id.setText(visitors.getId());
        holder.Sem.setText(visitors.getSem());
        holder.Branch.setText(visitors.getBranch());
        holder.time.setText(visitors.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
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
