package com.gonzaloandcompany.satapp.ui.home.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.Ticket;

import org.joda.time.LocalDate;

import java.util.List;

public class TicketAdapter extends ArrayAdapter<Ticket> {

    //Variables para poder usar los datos recibidos en el constructor
    Context ctx;
    int layoutPlantilla;
    List<Ticket> listaDatos;

    public TicketAdapter(@NonNull Context context, int resource, @NonNull List<Ticket> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.layoutPlantilla = resource;
        this.listaDatos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutPlantilla, parent,false);

        TextView tvTitulo = v.findViewById(R.id.textViewNomTicket);
        TextView tvFecha = v.findViewById(R.id.textViewFechaTicket);

        Ticket ticketActual = listaDatos.get(position);
        String titulo = ticketActual.getTitulo();
        LocalDate fechaCreacion = LocalDate.parse(ticketActual.getCreatedAt().substring(0, 10));


        tvTitulo.setText(titulo);
        tvFecha.setText(fechaCreacion.toString("dd/MM/yyyy"));

        return v;
    }
}
