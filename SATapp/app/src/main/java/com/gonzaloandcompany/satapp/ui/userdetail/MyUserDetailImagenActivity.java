package com.gonzaloandcompany.satapp.ui.userdetail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.UserService;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyUserDetailImagenActivity extends AppCompatActivity {
    @BindView(R.id.ivCancel)
    ImageView cancel;
    @BindView(R.id.ivSave)
    ImageView save;
    @BindView(R.id.ivEdit)
    ImageView change_image;
    @BindView(R.id.ivFotoInventariable)
    ImageView ivFoto;
    @BindView(R.id.ivDelete)
    ImageView delete;
    UserService service;
    private static final int READ_REQUEST_CODE = 42;
    Uri uriSelected;
    UsuarioDummy select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventariable_detaile_image);
        ButterKnife.bind(this);

        String id = getIntent().getExtras().getString("id");
        save.setVisibility(View.INVISIBLE);

        service = ApiSAT.createServicePeticiones(UserService.class, Constants.TOKEN_PROVISIONAL);

        Call<UsuarioDummy> call = service.getMyUser();
        call.enqueue(new Callback<UsuarioDummy>() {
            @Override
            public void onResponse(Call<UsuarioDummy> call, Response<UsuarioDummy> response) {

                if (response.isSuccessful()) {
                    select = response.body();
                    if(select.getPicture()!=null) {
                        GlideUrl glideUrl = new GlideUrl(Constants.BASE_URL + select.getPicture(),
                                new LazyHeaders.Builder()
                                        .addHeader("Authorization", "Bearer " + Constants.TOKEN_PROVISIONAL)
                                        .build());

                        Glide.with(MyUserDetailImagenActivity.this)
                                .load(glideUrl)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(ivFoto);

                        uriSelected = Uri.parse(select.getPicture());

                    }else{
                        Glide.with(MyUserDetailImagenActivity.this).load(R.drawable.iconfinder_unknown_403017).circleCrop().into(ivFoto);

                    }
                } else {
                    Toast.makeText(MyUserDetailImagenActivity.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioDummy> call, Throwable t) {
                Toast.makeText(MyUserDetailImagenActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyUserDetailImagenActivity.this, PerfilDetailActivity.class);
                startActivity(i);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyUserDetailImagenActivity.this);
                builder.setIcon(R.drawable.ic_warning);
                builder.setTitle("¿Está seguro de querer borrar la imagen de este dispositivo?");
                builder.setPositiveButton(R.string.delete, (dialogInterface, i) -> {
                    Call<Void> call1 = service.deleteUserImagen(id);
                    call1.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call1, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(MyUserDetailImagenActivity.this, "Imagen eliminada", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyUserDetailImagenActivity.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(MyUserDetailImagenActivity.this, PerfilDetailActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Void> call1, Throwable t) {
                            Toast.makeText(MyUserDetailImagenActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uriSelected != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uriSelected);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        int cantBytes;
                        byte[] buffer = new byte[1024 * 4];

                        while ((cantBytes = bufferedInputStream.read(buffer, 0, 1024 * 4)) != -1) {
                            baos.write(buffer, 0, cantBytes);
                        }

                        final RequestBody requestFile =
                                RequestBody.create(
                                        MediaType.parse(getContentResolver().getType(uriSelected)), baos.toByteArray());


                        Cursor cursor = getContentResolver().query(uriSelected, null, null, null, null);

                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        cursor.moveToFirst();
                        final String fileName = cursor.getString(nameIndex);

                        MultipartBody.Part imagen =
                                MultipartBody.Part.createFormData("imagen", fileName, requestFile);

                        Call<UsuarioDummy> updateImage = service.updateImageUser(id, imagen);

                        updateImage.enqueue(new Callback<UsuarioDummy>() {
                            @Override
                            public void onResponse(Call<UsuarioDummy> call, Response<UsuarioDummy> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(MyUserDetailImagenActivity.this, "Imagen modificada", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(MyUserDetailImagenActivity.this, PerfilDetailActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(MyUserDetailImagenActivity.this, "Error al modificar la imagen", Toast.LENGTH_LONG).show();
                                }

                                Log.d("RESPONSE UPDATE IMAGEN", ""+response);
                            }

                            @Override
                            public void onFailure(Call<UsuarioDummy> call, Throwable t) {
                                Toast.makeText(MyUserDetailImagenActivity.this, "Error de upload", Toast.LENGTH_LONG).show();
                            }
                        });


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {

                uri = resultData.getData();
                Glide.with(MyUserDetailImagenActivity.this)
                        .load(uri)
                        .centerCrop()
                        .into(ivFoto);
                uriSelected = uri;
                save.setVisibility(View.VISIBLE);
            }
        }
    }
}



