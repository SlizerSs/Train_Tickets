package com.example.train_tickets;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.train_tickets.Model.Ticket;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder >{
    ArrayList<Ticket> tickets;
    public String number_audit="";
    public TicketAdapter( ArrayList<Ticket> tickets){
        this.tickets=tickets;
    }
    public class TicketViewHolder extends RecyclerView.ViewHolder{
        TextView textViewFromWhere, textViewDate, textViewTime;
        public TicketViewHolder(View view) {
            super(view);
            textViewFromWhere=view.findViewById(R.id.textViewFromWhere);
            textViewDate=view.findViewById(R.id.textViewDate);
            textViewTime=view.findViewById(R.id.textViewTime);
        }
    }

    @NonNull
    @Override
    public TicketAdapter.TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tickets_list,parent,false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.TicketViewHolder  holder, final int position) {
        holder.textViewFromWhere.setText(tickets.get(position).getFrom()+" - "+tickets.get(position).getWhere());
        holder.textViewDate.setText(tickets.get(position).getDate());
        holder.textViewTime.setText(tickets.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}
