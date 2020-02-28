package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.modelos.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.Ticket;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ServicePeticiones {
    @GET("/ticket")
    Call<List<Ticket>> getTickets(@Query("page") int page);

    @Multipart
    @POST("/inventariable")
    Call<Inventariable> createInventariable(@Part MultipartBody.Part imagen,
                                            @Part("codigo") RequestBody codigo,
                                            @Part("tipo") RequestBody tipo,
                                            @Part("nombre") RequestBody nombre,
                                            @Part("descripcion") RequestBody descripcion,
                                            @Part("ubicaion") RequestBody ubicacion);

    @GET("/inventariable/tipos")
    Call<List<String>> getTiposInventariable();

    //TODO hablar con luismi
    @GET("/inventariable/tipos")
    Call<List<String>> getUbicaciones();
}
