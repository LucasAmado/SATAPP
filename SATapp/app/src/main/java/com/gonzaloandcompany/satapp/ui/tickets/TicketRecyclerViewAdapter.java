package com.gonzaloandcompany.satapp.ui.tickets;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.mymodels.Types;

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
        String aux;
        if (tickets != null) {
            holder.ticket = tickets.get(position);

            aux = holder.ticket.getTitulo().toUpperCase();
            holder.title.setText(aux);

            holder.description.setText(holder.ticket.getDescripcion());
            if (aux.contains(Types.IMPRESORA.getDescription())) {
                holder.img.setImageResource(R.drawable.ic_printer);
            } else if (aux.contains(Types.FOTOCOPIADORA.getDescription())) {
                holder.img.setImageResource(R.drawable.ic_copier);
            } else if (aux.contains(Types.RATON.getDescription())) {
                holder.img.setImageResource(R.drawable.ic_mouse);
            } else if (aux.contains(Types.TECLADO.getDescription())) {
                holder.img.setImageResource(R.drawable.ic_keyboard);
            } else if (aux.contains(Types.PROYECTOR.getDescription())) {
                holder.img.setImageResource(R.drawable.ic_projector);
            } else if (aux.contains(Types.ORDENADOR.getDescription()) || aux.contains(Types.PC.getDescription())) {
                holder.img.setImageResource(R.drawable.ic_computer);
            }
            LocalDate date = LocalDate.parse(holder.ticket.getFecha_creacion().substring(0, 10));
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
        Log.d("SET DATA ADAPTER",list.toString());
        this.tickets = list;
        notifyDataSetChanged();

    }

    public void addAll(List<Ticket> list) {
        int lastIndex = tickets.size() - 1;
        tickets.addAll(list);
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
