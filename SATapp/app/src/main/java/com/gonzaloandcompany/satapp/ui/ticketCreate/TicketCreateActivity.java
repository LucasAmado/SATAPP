package com.gonzaloandcompany.satapp.ui.ticketCreate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.JLuisViewModel;
import com.gonzaloandcompany.satapp.data.viewmodel.UserViewModel;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.requests.TicketCreateRequest;
import com.gonzaloandcompany.satapp.ui.tickets.TicketsViewModel;

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
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;

public class TicketCreateActivity extends AppCompatActivity {
    @BindView(R.id.ticketCreateAddImg)
    Button upload;
    @BindView(R.id.ticketCreatePreview)
    RecyclerView preview;
    @BindView(R.id.ticketCreateTech)
    TextView tech;
    @BindView(R.id.ticketCreateTItle)
    EditText title;
    @BindView(R.id.ticketCreateDescription)
    EditText description;
    @BindView(R.id.ticketCreateDevice)
    TextView device;
    @BindView(R.id.ticketCreateSave)
    Button save;
    @BindView(R.id.textView7)
    TextView techTitle;

    private List<Inventariable> devices;
    private List<Part> photos;
    private TicketsViewModel viewModel;
    private static final int RESULT_LOAD_IMAGE = 1;
    private UserViewModel userViewModel;
    private List<UsuarioDummy> techs;
    private String techId="";
    private String deviceId="";
    private JLuisViewModel jLuisViewModel;
    private UsuarioDummy currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_create);
        ButterKnife.bind(this);

        viewModel = new ViewModelProvider(this).get(TicketsViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        jLuisViewModel = new ViewModelProvider(this).get(JLuisViewModel.class);

        getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        preview.setLayoutManager(layoutManager);
        devices = new ArrayList<>();
        techs = new ArrayList<>();

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("Debes escribir un título");
                } else {
                    title.setError(null);
                }
            }
        });

        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (description.getText().toString().isEmpty()) {
                    description.setError("Debes describir qué problema ocurre");
                } else {
                    description.setError(null);
                }
            }
        });

        getDevices();
        getTechs();

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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
        Uri uri=Uri.fromFile(new File(image.getPath()));

        try {

            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int cantBytes;
            byte[] buffer = new byte[1024 * 4];

            while ((cantBytes = bufferedInputStream.read(buffer, 0, 1024 * 4)) != -1) {
                baos.write(buffer, 0, cantBytes);
            }
            if(uri!=null) {

                ContentResolver contentResolver = getContentResolver();

                RequestBody filePart =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), baos.toByteArray());
                img = Part.createFormData("fotos", image.getName(), filePart);
            }

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
                        deviceId,
                        techId,
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

    public void getDevices() {
        jLuisViewModel.getAllInventariables().observe(this, new Observer<List<Inventariable>>() {
            @Override
            public void onChanged(List<Inventariable> inventariables) {
                if(inventariables!=null||!inventariables.isEmpty()){
                    devices=inventariables;

                    if(!devices.isEmpty()){
                        device.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder dialog = new AlertDialog.Builder(TicketCreateActivity.this);
                                dialog.setTitle("Selecciona un dispositivo");
                                String[] namesDevices = new String[devices.size()];
                                for (int i = 0; i < namesDevices.length; i++) {
                                    namesDevices[i] = devices.get(i).getNombre();
                                }
                                dialog.setItems(namesDevices, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        device.setText(namesDevices[which]);
                                        deviceId = devices.get(which).getId();
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
                    }else{
                        device.setText("No hay ningún dispositivo dado de alta");
                        device.setEnabled(false);
                    }
                }
            }
        });


    }

    public void getTechs() {
        userViewModel.getUsers().observe(this, new Observer<List<UsuarioDummy>>() {
            @Override
            public void onChanged(List<UsuarioDummy> usuarios) {
                if (usuarios != null || !usuarios.isEmpty()) {
                    techs = usuarios.stream().filter(x -> x.getRole().equals("tecnico")).collect(Collectors.toList());

                    if(!techs.isEmpty()){
                        tech.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(TicketCreateActivity.this);
                                dialog.setTitle("Seleccione un técnico");
                                String[] namesTechs = new String[techs.size()];
                                for (int i = 0; i < namesTechs.length; i++) {
                                    namesTechs[i] = techs.get(i).getName();
                                }
                                dialog.setItems(namesTechs, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tech.setText(namesTechs[which]);
                                        techId = techs.get(which).getId();
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
                    }else{
                        tech.setText("No hay ningún técnico dado de alta");
                        tech.setEnabled(false);
                    }
                } else {
                    Toast.makeText(TicketCreateActivity.this, "No hay ningún técnico dado de alta", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void getCurrentUser(){
        userViewModel.getCurrentUser().observe(this, new Observer<UsuarioDummy>() {
            @Override
            public void onChanged(UsuarioDummy usuario) {
                currentUser=usuario;
                if(currentUser.getRole().equals("tecnico")||currentUser.getRole().equals("user")) {
                    tech.setVisibility(View.GONE);
                    techTitle.setVisibility(View.GONE);
                }else {
                    tech.setVisibility(View.VISIBLE);
                    techTitle.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}
