package com.gonzaloandcompany.satapp.data.repository;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.common.MyApp;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.ServicePeticiones;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LucasRepository {

    private ServicePeticiones service;

    public LucasRepository(){
        service = ApiSAT.createServicePeticiones(ServicePeticiones.class, Constants.TOKEN_PROVISIONAL);
    }

    public MutableLiveData<List<String>> getTipos(){
        final MutableLiveData<List<String>> data = new MutableLiveData<>();

        Call<List<String>> call = service.getTiposInventariable();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "Error recogiendo los tipos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
        return data;
    }

    public MutableLiveData<List<String>> getUbicaciones(){
        final MutableLiveData<List<String>> data = new MutableLiveData<>();

        Call<List<String>> call = service.getUbicaciones();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "Error cogiendo las ubicaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
        return data;
    }
}
