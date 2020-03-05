package com.gonzaloandcompany.satapp.ui.userdetail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.MyUserViewModel;
import com.gonzaloandcompany.satapp.requests.Password;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordDialogFragment extends DialogFragment {
    private View v;
    @BindView(R.id.editTextEmail)
    EditText etemail;
    @BindView(R.id.editTextPasswordOld)
    EditText etpasswordOld;
    @BindView(R.id.editTextPasswordNew)
    EditText etpasswordNew;
    String id;
    private MyUserViewModel userViewModel;

    public PasswordDialogFragment(String id) {
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Cambiar Contraseña");

        builder.setCancelable(true);
        v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_password, null);
        builder.setView(v);
        ButterKnife.bind(this,v);
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


                Password p = new Password(etpasswordNew.getText().toString());
                String email = etemail.getText().toString();
                String pOld = etpasswordOld.getText().toString();
                userViewModel.updatePassword(email,pOld,p,id);


            }

        });
        return builder.create();
    }
}
