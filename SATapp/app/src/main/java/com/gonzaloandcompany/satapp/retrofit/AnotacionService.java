package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.Anotacion;
import com.gonzaloandcompany.satapp.requests.CreateAnotacion;

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
    Call<Anotacion> createAnotacion(@Body CreateAnotacion createAnotacion);

    //TODO revisar
    @GET("/anotacion/ticket/{id}")
    Call<List<Anotacion>> getAnotacionesTicket(@Path("id") String id);

    //TODO solo se pasa el cuerpo, o al menos solo debería ser así
    @PUT("/anotacion/{id_anotacion}")
    Call<Anotacion> updateAnotacion(@Path("id_anotacion") String id_anotacion, @Body CreateAnotacion anotacion);

    @DELETE("/anotacion/{id_anotacion}")
    Call<Void> deleteAnotacion(@Path("id_anotacion") String id_anotacion);

    @GET("/anotacion/{id_anotacion}")
    Call<Anotacion> getAnotacion(@Path("id_anotacion") String id_anotacion);
}
