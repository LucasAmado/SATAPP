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
    EditText etNombre, etCodigo, etDescripcion;
    TextView tvImage, tvNombreImagen;
    Spinner spTipo, spUbicacion;
    String typeSelect, ubicationSelect, name, code, description, fileName;
    ImageView ivIcono;
    ArrayList<String> arrayTipos = new ArrayList<>(), arrayUbicaciones = new ArrayList<>();
    private static final int READ_REQUEST_CODE = 42;
    Uri uriSelected;
    ServicePeticiones service;
    String token = Constants.TOKEN_PROVISIONAL;
    LucasViewModel viewModel;
    ArrayAdapter<String> adapterTipos, adapterUbicaciones;
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
        etCodigo = v.findViewById(R.id.editTextCodigo);
        tvNombreImagen = v.findViewById(R.id.textViewtImage);
        tvImage = v.findViewById(R.id.textViewtImage);
        etDescripcion = v.findViewById(R.id.editTextDescripcion);
        spTipo = v.findViewById(R.id.spinnerTipo);
        spUbicacion = v.findViewById(R.id.spinnerUbicacion);
        ivIcono = v.findViewById(R.id.imageViewIcon);

        viewModel = new ViewModelProvider(getActivity()).get(LucasViewModel.class);

        loadTipos();

        loadUbicaciones();

        service = ApiSAT.createServicePeticiones(ServicePeticiones.class, token);

        if (idInventariable != null) {

            builder.setTitle("Editar dispositivo");
            builder.setMessage("Modifique los datos que desee del dispositivo");

            Call<Inventariable> call = service.getInventariableById(idInventariable);
            call.enqueue(new Callback<Inventariable>() {
                @Override
                public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                    if (response.isSuccessful()) {
                        inventariable_edit = response.body();
                        etNombre.setText(inventariable_edit.getNombre());
                        etCodigo.setText(inventariable_edit.getCodigo());
                        etDescripcion.setText(inventariable_edit.getDescripcion());
                        typeSelect = inventariable_edit.getTipo();
                        ubicationSelect = inventariable_edit.getUbicacion();
                        ivIcono.setVisibility(View.INVISIBLE);
                        tvNombreImagen.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<Inventariable> call, Throwable t) {

                }
            });
        }


        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelect = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ubicationSelect = (String) parent.getItemAtPosition(position);
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
                code = etCodigo.getText().toString();
                description = etDescripcion.getText().toString();

                if (!name.isEmpty() && !code.isEmpty() && !description.isEmpty() && !typeSelect.isEmpty() && !ubicationSelect.isEmpty()) {
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
                                        MultipartBody.Part.createFormData("avatar", fileName, requestFile);

                                RequestBody tipo = RequestBody.create(MultipartBody.FORM, typeSelect);
                                RequestBody nombre = RequestBody.create(MultipartBody.FORM, name);
                                RequestBody descripcion = RequestBody.create(MultipartBody.FORM, description);
                                RequestBody codigo = RequestBody.create(MultipartBody.FORM, description);
                                RequestBody ubicacion = RequestBody.create(MultipartBody.FORM, ubicationSelect);

                                Call<Inventariable> create = service.createInventariable(imagen, codigo, tipo, nombre, descripcion, ubicacion);

                                create.enqueue(new Callback<Inventariable>() {
                                    @Override
                                    public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                                        Log.e("RESPONSE", ""+response);
                                    }

                                    @Override
                                    public void onFailure(Call<Inventariable> call, Throwable t) {
                                        Toast.makeText(getActivity(), "Error de conexión al intentar crear", Toast.LENGTH_SHORT).show();
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
                        inventariable_edit = new Inventariable(code, typeSelect, name, description, ubicationSelect);
                        Call<Inventariable> update = service.updateInventariable(idInventariable, inventariable_edit);
                        update.enqueue(new Callback<Inventariable>() {
                            @Override
                            public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                                Log.d("RESPONSE EDITAR", ""+response.body());
                                dialog.dismiss();
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

                    if (code.isEmpty()) {
                        etCodigo.setError("Introduzca un código");
                    }

                    if (typeSelect.isEmpty() || ubicationSelect.isEmpty()) {
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

    public void loadUbicaciones() {
        viewModel.getAllUbicaciones().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> ubicaciones) {

                for (int i = 0; i < ubicaciones.size(); i++) {
                    arrayUbicaciones.add(ubicaciones.get(i));
                }

                adapterUbicaciones = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, arrayUbicaciones);
                adapterUbicaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spUbicacion.setAdapter(adapterUbicaciones);
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

}