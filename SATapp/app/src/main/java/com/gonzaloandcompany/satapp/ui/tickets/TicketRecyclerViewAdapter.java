package com.gonzaloandcompany.satapp.ui.tickets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.MyApp;
import com.gonzaloandcompany.satapp.mymodels.Ticket;

import org.joda.time.LocalDate;

import java.util.List;


public class TicketRecyclerViewAdapter extends RecyclerView.Adapter<TicketRecyclerViewAdapter.ViewHolder> {
    private List<Ticket> tickets;
    private final TicketListener listener;

    public TicketRecyclerViewAdapter(List<Ticket> tickets, TicketListener listener) {
        this.tickets = tickets;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tickets, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (tickets != null) {
            holder.ticket = tickets.get(position);
            Glide.with(MyApp.getContext()).load("").into(holder.img);
            holder.title.setText(holder.ticket.getTitulo());
            holder.description.setText(holder.ticket.getDescripcion());
            LocalDate date = LocalDate.parse(holder.ticket.getFecha_creacion());
            holder.date.setText(date.toString("dd/MM/yyyy"));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTicketClick(holder.ticket.getId());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (tickets != null)
            return tickets.size();
        else
            return 0;
    }

    public void setData(List<Ticket> list) {
        this.tickets = list;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Ticket ticket;
        public ImageView img;
        public TextView title;
        public TextView description;
        public TextView date;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            img = view.findViewById(R.id.ticketListImg);
            title = view.findViewById(R.id.ticketListTitle);
            description = view.findViewById(R.id.ticketListDescription);
            date = view.findViewById(R.id.ticketListDate);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mView=" + mView +
                    ", ticket=" + ticket +
                    ", img=" + img +
                    ", title=" + title +
                    ", description=" + description +
                    ", date=" + date +
                    '}';
        }
    }

}
