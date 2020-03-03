package com.gonzaloandcompany.satapp.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.JLuisViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterDialogFragment extends DialogFragment {
    private View v;
    private ListView lvFiltro;
    private List<String> listaMostrar = new ArrayList<>();
    private DialogPassData dialogPassData;


    public FilterDialogFragment(DialogPassData dialogPassData, List<String> ubicaciones) {
        this.dialogPassData = dialogPassData;
        this.listaMostrar = ubicaciones;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Configura el dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Configuración del diálogo

        builder.setTitle("Filtro de Ubicaciones");
        builder.setMessage("Selecciona una ubicacion");

        // Hacer que el diálogo sólo se pueda cerrar desde el botón
        // Cancelar o Guardar
        builder.setCancelable(true);

        // Cargar el layout del formulario
        v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_filtro_ubicacion, null);
        builder.setView(v);

        lvFiltro = v.findViewById(R.id.lvFiltro);
        Collections.sort(listaMostrar);

        DispositivoFilterAdapter adapter = new DispositivoFilterAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                listaMostrar
        );


        lvFiltro.setAdapter(adapter);


        builder.setNegativeButton(R.string.button_cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancelar diálogo
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        lvFiltro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) lvFiltro.getItemAtPosition(position);
                dialogPassData = (DialogPassData) getTargetFragment();

                dialogPassData.filterByUbicacion(item);


                dialog.dismiss();
            }

        });

        // Create the AlertDialog object and return it

        return dialog;
    }
}

