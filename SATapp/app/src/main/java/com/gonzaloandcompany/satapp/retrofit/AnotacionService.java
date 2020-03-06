package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.Anotacion;
import com.gonzaloandcompany.satapp.mymodels.CreateAnotacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AnotacionService {

    @POST("/anotacion")
    Call<CreateAnotacion> createAnotacion(@Body CreateAnotacion createAnotacion);

    //TODO revisar
    @GET("/anotaciones/ticket/{id_ticket}")
    Call<List<Anotacion>> getAnotacionesTicket(@Path("id_ticket") String id_ticket);

    //TODO solo se pasa el cuerpo, o al menos solo debería ser así
    @PUT("/anotaciones/{id_anotacion}")
    Call<CreateAnotacion> updateAnotacion(@Path("id_anotacion") String id_anotacion, @Body CreateAnotacion anotacion);

    @DELETE("/anotaciones/{id_anotacion}")
    Call<Void> deleteAnotacion(@Path("id_anotacion") String id_anotacion);

    @GET("/anotaciones/{id_anotacion}")
    Call<Anotacion> getAnotacion(@Path("id_anotacion") String id_anotacion);
}
