package com.gonzaloandcompany.satapp.ui.home.detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.data.viewmodel.JLuisViewModel;
import com.gonzaloandcompany.satapp.data.viewmodel.LucasViewModel;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.ServicePeticiones;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventariableDialogFragment extends DialogFragment {

    private View v;
    EditText etNombre, etDescripcion, etUbicacion;
    TextView tvImage, tvNombreImagen;
    Spinner spTipo;
    String typeSelect, ubication, name, description, fileName;
    ImageView ivIcono;
    ArrayList<String> arrayTipos = new ArrayList<>();
    private static final int READ_REQUEST_CODE = 42;
    Uri uriSelected;
    ServicePeticiones service;
    String token = Constants.TOKEN_PROVISIONAL;
    LucasViewModel viewModel;
    JLuisViewModel jLuisViewModel;
    ArrayAdapter<String> adapterTipos;
    String idInventariable;
    Inventariable inventariable_edit;

    public InventariableDialogFragment(String id) {
        this.idInventariable = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Nuevo dispositivo");
        builder.setMessage("Introduzca los datos del dispositivo nuevo");

        builder.setCancelable(true);

        v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_inventariable, null);
        builder.setView(v);

        etNombre = v.findViewById(R.id.editTextNombre);
        tvNombreImagen = v.findViewById(R.id.textViewtImage);
        tvImage = v.findViewById(R.id.textViewtImage);
        etDescripcion = v.findViewById(R.id.editTextDescripcion);
        etUbicacion = v.findViewById(R.id.editTextUbicacion);
        spTipo = v.findViewById(R.id.spinnerTipo);
        ivIcono = v.findViewById(R.id.imageViewIcon);

        viewModel = new ViewModelProvider(getActivity()).get(LucasViewModel.class);
        jLuisViewModel = new ViewModelProvider(getActivity()).get(JLuisViewModel.class);

        if (idInventariable != null)
            loadDispositivo();

        loadTipos();

        service = ApiSAT.createServicePeticiones(ServicePeticiones.class, token);


        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelect = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ivIcono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });


        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                name = etNombre.getText().toString();
                description = etDescripcion.getText().toString();
                ubication = etUbicacion.getText().toString();

                if (!name.isEmpty() && !description.isEmpty() && !typeSelect.isEmpty() && !ubication.isEmpty()) {
                    //crear
                    if (idInventariable == null) {
                        if (uriSelected == null) {
                            tvImage.setError("Seleccione una foto");
                        } else {
                            try {
                                InputStream inputStream = getActivity().getContentResolver().openInputStream(uriSelected);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                                int cantBytes;
                                byte[] buffer = new byte[1024 * 4];

                                while ((cantBytes = bufferedInputStream.read(buffer, 0, 1024 * 4)) != -1) {
                                    baos.write(buffer, 0, cantBytes);
                                }

                                final RequestBody requestFile =
                                        RequestBody.create(
                                                MediaType.parse(getActivity().getContentResolver().getType(uriSelected)), baos.toByteArray());

                                MultipartBody.Part imagen =
                                        MultipartBody.Part.createFormData("imagen", fileName, requestFile);

                                RequestBody tipo = RequestBody.create(MultipartBody.FORM, typeSelect);
                                RequestBody nombre = RequestBody.create(MultipartBody.FORM, name);
                                RequestBody descripcion = RequestBody.create(MultipartBody.FORM, description);
                                RequestBody ubicacion = RequestBody.create(MultipartBody.FORM, ubication);

                                Call<Inventariable> create = service.createInventariable(imagen, tipo, nombre, descripcion, ubicacion);

                                create.enqueue(new Callback<Inventariable>() {
                                    @Override
                                    public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                                        Log.e("RESPONSE", "" + response);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<Inventariable> call, Throwable t) {
                                        Log.e("RESPONSE ERROR", "ERROR DE CONEXIÓN AL INTENTAR CREAR");
                                    }
                                });


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        //Editar
                    } else {
                        inventariable_edit = new Inventariable(typeSelect, name, description, ubication);
                        Call<Inventariable> update = service.updateInventariable(idInventariable, inventariable_edit);
                        update.enqueue(new Callback<Inventariable>() {
                            @Override
                            public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                                if (response.isSuccessful()) {
                                    Log.d("RESPONSE EDITAR", "" + response.body());

                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Inventariable> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error de conexión al editar", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                } else {

                    if (name.isEmpty()) {
                        etNombre.setError("Introduzca un nombre");
                    }

                    if (description.isEmpty()) {
                        etDescripcion.setError("Introduzca una descripción");
                    }


                    if (typeSelect.isEmpty() || ubication.isEmpty()) {
                        Toast.makeText(getActivity(), "No olvide elegir un tipo y la ubicación", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        return builder.create();
    }

    public void loadTipos() {
        viewModel.getAllTipos().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> tipos) {

                for (int i = 0; i < tipos.size(); i++) {
                    arrayTipos.add(tipos.get(i));
                }

                adapterTipos = new ArrayAdapter<String>(InventariableDialogFragment.this.getActivity(),
                        android.R.layout.simple_spinner_item, arrayTipos);
                adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTipo.setAdapter(adapterTipos);
            }
        });
    }


    public void performFileSearch() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                uriSelected = uri;

                Cursor cursor = getActivity().getContentResolver().query(uriSelected, null, null, null, null);
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                cursor.moveToFirst();
                fileName = cursor.getString(nameIndex);
                tvImage.setText(fileName);

            }
        }
    }


    public void loadDispositivo() {
        viewModel.getInventariable(idInventariable).observe(getActivity(), new Observer<Inventariable>() {
            @Override
            public void onChanged(Inventariable inventariable) {
                inventariable_edit = inventariable;
                etNombre.setText(inventariable_edit.getNombre());
                etDescripcion.setText(inventariable_edit.getDescripcion());
                typeSelect = inventariable_edit.getTipo();
                etUbicacion.setText(inventariable_edit.getUbicacion());
                ivIcono.setVisibility(View.INVISIBLE);
                tvNombreImagen.setVisibility(View.INVISIBLE);
            }
        });
    }

}