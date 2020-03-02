package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.Ticket;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface InventriableService {
    @GET("/inventariable")
    Call <List<Inventariable>> getAllInventariable();

    @GET("/inventariable/ubicaciones")
    Call <List<String>> getAllUbicaciones();
}
