package com.gonzaloandcompany.satapp.ui.anotaciones;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.AnotacionViewModel;
import com.gonzaloandcompany.satapp.mymodels.Anotacion;
import com.gonzaloandcompany.satapp.mymodels.CreateAnotacion;

public class AnotacionDIalogFragment extends DialogFragment {
    private View v;
    String idTicket, idAnotacion;
    EditText etCuerpo;
    AnotacionViewModel anotacionViewModel;

    public AnotacionDIalogFragment(String idTicket, String idAnotacion){
        this.idTicket = idTicket;
        this.idAnotacion = idAnotacion;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        etCuerpo = v.findViewById(R.id.editTextCuerpo);

        anotacionViewModel = new ViewModelProvider(getActivity()).get(AnotacionViewModel.class);

        v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_anotaciones, null);

        if(idAnotacion==null){
            builder.setTitle("Nueva anotación");
        }else {
            builder.setTitle("Editar anotación");
            loadAnotacion();
        }

        builder.setCancelable(true);
        builder.setView(v);


        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(idAnotacion==null){
                    CreateAnotacion new_anotacion = new CreateAnotacion(idTicket, etCuerpo.getText().toString());
                    anotacionViewModel.createAnotacion(new_anotacion).observe(getActivity(), new Observer<CreateAnotacion>() {
                        @Override
                        public void onChanged(CreateAnotacion createAnotacion) {
                            if(createAnotacion!=null){
                                dialog.dismiss();
                            }
                        }
                    });
                }else{
                    CreateAnotacion anotacion_edit = new CreateAnotacion(etCuerpo.getText().toString());
                    anotacionViewModel.updateAnotacion(idAnotacion, anotacion_edit).observe(getActivity(), new Observer<CreateAnotacion>() {
                        @Override
                        public void onChanged(CreateAnotacion edit) {
                            if(edit!=null){
                                dialog.dismiss();
                            }
                        }
                    });
                }

            }
        });

        return builder.create();
    }


    public void loadAnotacion(){
        anotacionViewModel.getAnotacion(idAnotacion).observe(this, new Observer<Anotacion>() {
            @Override
            public void onChanged(Anotacion anotacion) {
                if(anotacion!=null){
                    etCuerpo.setText(anotacion.getCuerpo());
                }
            }
        });
    }
}
