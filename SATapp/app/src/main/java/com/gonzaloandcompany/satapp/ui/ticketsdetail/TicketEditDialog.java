package com.gonzaloandcompany.satapp.ui.ticketsdetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;


import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.requests.TicketUpdateRequest;
import com.gonzaloandcompany.satapp.ui.tickets.TicketsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketEditDialog  extends DialogFragment {
    View view;
    @BindView(R.id.ticket_edit_title)
    EditText title;
    @BindView(R.id.ticket_edit_description)
    EditText description;
    private TicketsViewModel ticketsViewModel;
    private String id;
    private Activity activity;

    public TicketEditDialog(String id, TicketsViewModel ticketsViewModel, Activity activity){
        this.ticketsViewModel = ticketsViewModel;
        this.id= id;
        this.activity= activity;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_ticket_edit, null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setView(view);

        dialogBuilder.setCancelable(true);

        dialogBuilder.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TicketUpdateRequest request = new TicketUpdateRequest(title.getText().toString(), description.getText().toString());
                ticketsViewModel.update(id, request).observe(getActivity(), new Observer<Ticket>() {
                    @Override
                    public void onChanged(Ticket ticket) {
                        dialog.dismiss();
                        activity.recreate();
                    }
                });
            }
        });

        return  dialogBuilder.create();
    }
}
