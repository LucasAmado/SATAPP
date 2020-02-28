package com.gonzaloandcompany.satapp.ui.ticketCreate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.requests.TicketCreateRequest;
import com.gonzaloandcompany.satapp.ui.tickets.TicketsViewModel;
import com.gonzaloandcompany.satapp.ui.ticketsdetail.TicketDetailActivity;
import com.gonzaloandcompany.satapp.ui.ticketsdetail.TicketImagesAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;

public class TicketCreateActivity extends AppCompatActivity {
    @BindView(R.id.ticketCreateAddImg)
    Button upload;
    @BindView(R.id.ticketCreatePreview)
    RecyclerView preview;
    @BindView(R.id.ticketCreateTech)
    EditText tech;
    @BindView(R.id.ticketCreateTItle)
    EditText title;
    @BindView(R.id.ticketCreateDescription)
    EditText description;
    @BindView(R.id.ticketCreateDevice)
    EditText device;

    private List<Inventariable> devices;
    private List<Part> photos;
    private TicketsViewModel viewModel;
    private static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_create);
        ButterKnife.bind(this);

        viewModel = new ViewModelProvider(this).get(TicketsViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        preview.setLayoutManager(layoutManager);


        //TODO: COMPLETAR LOS SIGUIENTES MÉTODOS: getDevices(), getTechs()
        getDevices();
        getTechs();

        device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(TicketCreateActivity.this);
                dialog.setTitle("Selecciona un dispositivo");
                String [] namesDevices= new String[devices.size()];
                for(int i =0;i<namesDevices.length ;i++){
                    namesDevices[i]=devices.get(i).getNombre();
                }
                dialog.setItems(namesDevices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        device.setText(namesDevices[which]);
                    }
                });

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });
        //TODO: ESCONDER ESTE BOTÓN SEGÚN SEA EL ROL DEL USUARIO
        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.create(TicketCreateActivity.this)
                        .language("es")
                        .limit(4)
                        .folderMode(true)
                        .start(RESULT_LOAD_IMAGE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE) {
            if (data != null) {
                List<Image> images = ImagePicker.getImages(data);

                TicketCreateAdapter adapter = new TicketCreateAdapter(images, TicketCreateActivity.this);
                preview.setAdapter(adapter);
                preview.setVisibility(View.VISIBLE);

                photos = new ArrayList<>();
                for (Image i : images) {
                    photos.add(createMultipart(i));
                }
            }
        }
    }

    private Part createMultipart(Image image) {
        Part img = null;

        try {

            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(image.getPath()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int cantBytes;
            byte[] buffer = new byte[1024 * 4];

            while ((cantBytes = bufferedInputStream.read(buffer, 0, 1024 * 4)) != -1) {
                baos.write(buffer, 0, cantBytes);
            }
            RequestBody filePart =
                    RequestBody.create(
                            MediaType.parse(getContentResolver().getType(Uri.parse(image.getPath()))), baos.toByteArray());


            img = Part.createFormData("fotos", image.getName(), filePart);

        } catch (IOException exception) {
            Log.d("EXCEPTION UPLOAD", exception.getMessage());
        }

        return img;
    }

    public void save() {
        TicketCreateRequest request =
                new TicketCreateRequest(
                        title.getText().toString(),
                        description.getText().toString(),
                        device.getText().toString(),
                        tech.getText().toString(),
                        photos);

        viewModel.createTicket(request).observe(this, new Observer<Ticket>() {
            @Override
            public void onChanged(Ticket ticket) {
                if (ticket != null) {
                    finish();
                } else {
                    Toast.makeText(TicketCreateActivity.this, "No se ha podido crear un nuevo ticket.\n\nContacte con el servicio técnico", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getDevices(){

    }

    public void getTechs(){

    }
}
