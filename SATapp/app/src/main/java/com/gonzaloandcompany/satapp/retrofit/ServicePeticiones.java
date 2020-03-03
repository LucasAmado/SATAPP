package com.gonzaloandcompany.satapp.retrofit;

import android.net.Uri;

import com.gonzaloandcompany.satapp.mymodels.Ticket;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
                                            @Part("ubicacion") RequestBody ubicacion);

    @GET("/inventariable/tipos")
    Call<List<String>> getTiposInventariable();

    //TODO hablar con luismi
    @GET("/inventariable/tipos")
    Call<List<String>> getUbicaciones();

    @GET("/inventariable/{id_inventariable}")
    Call<Inventariable> getInventariableById(@Path("id_inventariable") String id_inventariable);

    //TODO averiguar que devuelve
    @GET("/inventariable/img/{id_inventariable}")
    Call<Uri> getImageInventariable(@Path("id_inventariable") String id_inventariable);

    @PUT("/inventariable/{id}")
    Call<Inventariable> updateInventariable(@Path("id") String id, @Body Inventariable inventariable);

    @DELETE("/inventariable/{id}")
    Call<Inventariable> deleteInventariable(@Path("id") String id);
}
