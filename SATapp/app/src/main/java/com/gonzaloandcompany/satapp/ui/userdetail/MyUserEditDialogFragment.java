package com.gonzaloandcompany.satapp.ui.userdetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.MyUserViewModel;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.requests.EditUsers;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyUserEditDialogFragment extends DialogFragment {
    private View v;
    @BindView(R.id.editTextNombre)
    EditText etNombre;
    String name1;
    String id;
    Context act;
    private MyUserViewModel userViewModel;

    public MyUserEditDialogFragment(String id, Context act) {
        this.act=act;
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nuevo nombre");

        builder.setCancelable(true);
        v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_perfil, null);
        ButterKnife.bind(this, v);
        builder.setView(v);

        userViewModel = new ViewModelProvider(this).get(MyUserViewModel.class);

        builder.setNegativeButton(R.string.button_cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancelar diálogo
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                name1 = etNombre.getText().toString();
                EditUsers name = new EditUsers(name1);
                userViewModel.updateMyUser(id, name).observe(((PerfilDetailActivity)act), new Observer<UsuarioDummy>() {
                    @Override
                    public void onChanged(UsuarioDummy usuarioDummy) {
                        ((PerfilDetailActivity)act).onResume();
                    }
                });


            }

        });
        return builder.create();
    }
}

