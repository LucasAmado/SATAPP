package com.gonzaloandcompany.satapp.data.repository;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.common.MyApp;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.ServicePeticiones;

import java.time.format.ResolverStyle;
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

    public MutableLiveData<Inventariable> getInventariableById(String id_inventariable){
        final MutableLiveData<Inventariable> data = new MutableLiveData<>();
        Call<Inventariable> call = service.getInventariableById(id_inventariable);
        call.enqueue(new Callback<Inventariable>() {
            @Override
            public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "Error al cargar el dispositivo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Inventariable> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Erroe de conexión al intentar coger un dispositivo", Toast.LENGTH_SHORT).show();
            }
        });
        return data;
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

    public MutableLiveData<List<Ticket>> getTicketsInventariable(String id_inventariable){
        final MutableLiveData<List<Ticket>> data = new MutableLiveData<>();

        Call<List<Ticket>> callTickets = service.getTicketsInventariable(id_inventariable);
        callTickets.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "Error al cargar los tickets del dispositivo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
        return data;
    }

    public MutableLiveData<Inventariable> updateInventariable(String idInventariable, Inventariable inventariable_edit){
        final MutableLiveData<Inventariable> data = new MutableLiveData<>();
        Call<Inventariable> update = service.updateInventariable(idInventariable, inventariable_edit);
        update.enqueue(new Callback<Inventariable>() {
            @Override
            public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                if(response.isSuccessful()) {
                    Log.d("RESPONSE EDITAR", "" + response.body());
                    data.setValue(response.body());
                    Toast.makeText(MyApp.getContext(), "Dipositivo editado correctamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Inventariable> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de conexión al editar", Toast.LENGTH_SHORT).show();

            }
        });
        return data;
    }
}
