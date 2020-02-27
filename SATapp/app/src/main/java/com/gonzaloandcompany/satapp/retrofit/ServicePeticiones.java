package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.modelos.Inventariable;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServicePeticiones {

    @Multipart
    @POST("/inventariable")
    Call<Inventariable> createInventariable(@Part MultipartBody.Part imagen,
                                            @Part("codigo") RequestBody codigo,
                                            @Part("tipo") RequestBody tipo,
                                            @Part("nombre") RequestBody nombre,
                                            @Part("descripcion") RequestBody descripcion);

    @GET("/inventariable/tipos")
    Call<List<String>> getTiposInventariable();

    //TODO habalr con luismi. Error 500
    /*@GET("/inventariable/tipos")
    Call<List<String>> getUbicaciones();*/
}
