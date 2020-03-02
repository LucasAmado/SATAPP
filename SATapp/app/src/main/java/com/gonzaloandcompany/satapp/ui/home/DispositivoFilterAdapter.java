package com.gonzaloandcompany.satapp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DispositivoFilterAdapter extends ArrayAdapter<String> {

    Context ctx;
    int layoutPlantilla;
    List<String> listaDatos;

    public DispositivoFilterAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.layoutPlantilla = resource;
        this.listaDatos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutPlantilla, parent, false);

        // Rescatar una referencia de cada elemento visual de la plantilla (template)
        TextView tvNombre = v.findViewById(android.R.id.text1);

        // Obtener los datos del país actual que debo dibujar
        String opcActual = listaDatos.get(position);


        // Insertar en los componentes de la plantilla
        // los datos del país actual
        tvNombre.setText(opcActual);

        return v;
    }

}
