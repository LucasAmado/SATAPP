package com.gonzaloandcompany.satapp.ui.home.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.gonzaloandcompany.satapp.MainActivity;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.data.viewmodel.InventariableDetailViewModel;
import com.gonzaloandcompany.satapp.data.viewmodel.LucasViewModel;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.ServicePeticiones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventariableDetailActivity extends AppCompatActivity {
    String idInventariable, urlImagen;
    ServicePeticiones service;
    @BindView(R.id.textViewName)
    TextView tvNombre;
    @BindView(R.id.textViewCode)
    TextView tvCodigo;
    @BindView(R.id.textViewType)
    TextView tvTipo;
    @BindView(R.id.textViewCreatedAt)
    TextView tvCreado;
    @BindView(R.id.textViewUpdatedAt)
    TextView tvModificado;
    @BindView(R.id.textViewDescription)
    TextView tvDescripcion;
    @BindView(R.id.imageViewHeader)
    ImageView ivFoto;
    @BindView(R.id.imageViewEdit)
    ImageView icon_edit;
    @BindView(R.id.imageViewDelete)
    ImageView icon_delete;
    @BindView(R.id.edit_image_inventariable)
    FloatingActionButton edit_image;
    @BindView(R.id.lvTicketsInventariable)
    ListView lvTickets;
    List<Ticket> ticketList = new ArrayList<>();
    LucasViewModel viewModel;
    InventariableDetailViewModel detailViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_inventariable_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        idInventariable = extras.getString(Constants.ID_INVENTARIABLE);

        viewModel = new ViewModelProvider(InventariableDetailActivity.this).get(LucasViewModel.class);
        detailViewModel = new ViewModelProvider(InventariableDetailActivity.this).get(InventariableDetailViewModel.class);

        service = ApiSAT.createServicePeticiones(ServicePeticiones.class, Constants.TOKEN_PROVISIONAL);

        loadDispositivo();
        loadTicketsDispositivo();


        icon_delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(InventariableDetailActivity.this);
            builder.setIcon(R.drawable.ic_warning);
            builder.setTitle("¿Está seguro de querer borrar este dispositivo?");
            builder.setMessage("Una vez borrado no se podrá deshacer esta decisión");
            builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    detailViewModel.deleteInventariable(idInventariable);
                    Intent intent = new Intent(InventariableDetailActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            builder.show();
        });

        icon_edit.setOnClickListener(v -> {
            DialogFragment dialog = new InventariableDialogFragment(idInventariable);
            dialog.show(getSupportFragmentManager(), "InventariableDialogFragment");
        });

        edit_image.setOnClickListener(v -> {
            Intent i = new Intent(InventariableDetailActivity.this, InventariableDetaileImageActivity.class);
            i.putExtra(Constants.ID_INVENTARIABLE, idInventariable);
            startActivity(i);
        });

    }

    public void loadDispositivo() {
        viewModel.getInventariable(idInventariable).observe(InventariableDetailActivity.this, select -> {
            LocalDate fechaCreacion = LocalDate.parse(select.getCreatedAt().substring(0, 10));
            LocalDate fechaCambio = LocalDate.parse(select.getUpdatedAt().substring(0, 10));

            tvNombre.setText(select.getNombre());
            tvCodigo.setText(select.getCodigo());
            tvTipo.setText(select.getTipo());
            tvCreado.setText(fechaCreacion.toString("dd/MM/yyyy"));
            tvModificado.setText(fechaCambio.toString("dd/MM/yyyy"));
            tvDescripcion.setText(select.getDescripcion());
            urlImagen = select.getImagen();

            GlideUrl glideUrl = new GlideUrl(Constants.BASE_URL + urlImagen,
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + Constants.TOKEN_PROVISIONAL)
                            .build());

            Glide.with(InventariableDetailActivity.this)
                    .load(glideUrl)
                    .centerCrop()
                    .into(ivFoto);
        });
    }

    public void loadTicketsDispositivo() {
        detailViewModel.getTicketsInventariable(idInventariable).observe(InventariableDetailActivity.this, tickets -> {
            ticketList = tickets;
            TicketAdapter adapter = new TicketAdapter(
                    InventariableDetailActivity.this,
                    R.layout.lista_ticket_inventariable,
                    ticketList
            );
            lvTickets.setAdapter(adapter);
        });

    }
}
