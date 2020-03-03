package com.gonzaloandcompany.satapp.data.repository;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.common.MyApp;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.InventriableService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JLuisRepository {

    InventriableService service;

    public JLuisRepository() {
        this.service = ApiSAT.createServicePeticiones(InventriableService.class, Constants.TOKEN_PROVISIONAL);
    }


    public MutableLiveData<List<Inventariable>> getAllInventariable() {
        final MutableLiveData<List<Inventariable>> data = new MutableLiveData<>();
        Call<List<Inventariable>> call = service.getAllInventariable();
        call.enqueue(new Callback<List<Inventariable>>() {
            @Override
            public void onResponse(Call<List<Inventariable>> call, Response<List<Inventariable>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Inventariable>> call, Throwable t) {

            }
        });
        return data;
    }

    public MutableLiveData<List<String>> getAllUbicaciones() {
        final MutableLiveData<List<String>> data = new MutableLiveData<>();
        Call<List<String>> call = service.getAllUbicaciones();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
        return data;
    }

    public MutableLiveData<Void> getImagenInventariable() {
        final MutableLiveData<Void> data = new MutableLiveData<>();
        Call<Void> call = service.getImagenIventariable();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        return data;
    }
}
