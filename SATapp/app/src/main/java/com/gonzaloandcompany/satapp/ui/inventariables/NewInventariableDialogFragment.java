package com.gonzaloandcompany.satapp.ui.inventariables;

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

import com.bumptech.glide.Glide;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.common.MySharedPreferences;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.ServicePeticiones;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NewInventariableDialogFragment extends DialogFragment {

    private View v;
    EditText etNombre, etCodigo, etDescripcion;
    TextView tvImage;
    Spinner spTipo, spUbicacion;
    String typeSelect, ubicationSelect, name, code, description, fileName;
    ImageView ivIcono;
    String [] tipos, ubicaciones={"1"};
    private static final int READ_REQUEST_CODE = 42;
    Uri uriSelected;
    ServicePeticiones service;
    String token = Constants.TOKEN_PROVISIONAL;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Nuevo dispositivo");
        builder.setMessage("Introduzca los datos del dispositivo nuevo");

       builder.setCancelable(true);

        v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_inventariable, null);
        builder.setView(v);

        etNombre = v.findViewById(R.id.editTextNombre);
        etCodigo = v.findViewById(R.id.editTextCodigo);
        tvImage = v.findViewById(R.id.editTextImage);
        etDescripcion = v.findViewById(R.id.editTextDescripcion);
        spTipo = v.findViewById(R.id.spinnerTipo);
        spUbicacion = v.findViewById(R.id.spinnerUbicacion);
        ivIcono = v.findViewById(R.id.imageViewIcon);

        ArrayAdapter<String> adapterTipos = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, tipos);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterTipos);

        ArrayAdapter<String> adapterUbicaciones = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, ubicaciones);
        adapterUbicaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUbicacion.setAdapter(adapterUbicaciones);



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


        builder.setNegativeButton(R.string.button_cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                name = etNombre.getText().toString();
                code = etCodigo.getText().toString();
                description = etDescripcion.getText().toString();


                if (uriSelected != null && !name.isEmpty() && !code.isEmpty() && !description.isEmpty()) {
                    service = ApiSAT.createServicePeticiones(ServicePeticiones.class, token);
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

                        //TODO ViewModel y repository --> meter allí la llamada a la api para crear dispositivo
                        /*Call<Inventariable> call = service.createInventariable(imagen, tipo, nombre, descripcion, ubicacion);

                        call.enqueue(new Callback<Inventariable>() {
                            @Override
                            public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Dispositivo creado correctamente", Toast.LENGTH_LONG).show();
                                    getDialog().dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Error inesperado", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Inventariable> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error de upload", Toast.LENGTH_LONG).show();
                            }
                        });*/


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }else{

                    if(name.isEmpty()){
                        etNombre.setError("Introduzca un nombre");
                    }

                    if(description.isEmpty()){
                        etDescripcion.setError("Introduzca una descripción");
                    }

                    if(code.isEmpty()){
                        etCodigo.setError("Introduzca un código");
                    }

                    if(uriSelected==null){
                        tvImage.setError("Seleccione una foto");
                    }

                    if(typeSelect.isEmpty() || ubicationSelect.isEmpty()){
                        Toast.makeText(getActivity(), "No olvide elegir un tipo y la ubicación", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        return builder.create();
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
