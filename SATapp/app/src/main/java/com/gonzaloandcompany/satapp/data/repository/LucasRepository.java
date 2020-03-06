package com.gonzaloandcompany.satapp.data.repository;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.MainActivity;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.common.MyApp;
import com.gonzaloandcompany.satapp.mymodels.Anotacion;
import com.gonzaloandcompany.satapp.requests.CreateAnotacion;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.retrofit.AnotacionService;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.ServicePeticiones;
import com.gonzaloandcompany.satapp.ui.home.detail.InventariableDetailActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LucasRepository {

    private ServicePeticiones service;
    private AnotacionService anotacionService;

    public LucasRepository() {
        service = ApiSAT.createServicePeticiones(ServicePeticiones.class, Constants.TOKEN_PROVISIONAL);
        anotacionService = ApiSAT.createServicePeticiones(AnotacionService.class, Constants.TOKEN_PROVISIONAL);
    }

    public MutableLiveData<Inventariable> getInventariableById(String id_inventariable) {
        final MutableLiveData<Inventariable> data = new MutableLiveData<>();
        Call<Inventariable> call = service.getInventariableById(id_inventariable);
        call.enqueue(new Callback<Inventariable>() {
            @Override
            public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Inventariable> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Erroe de conexión al intentar coger un dispositivo", Toast.LENGTH_SHORT).show();
            }
        });
        return data;
    }

    public MutableLiveData<List<String>> getTipos() {
        final MutableLiveData<List<String>> data = new MutableLiveData<>();

        Call<List<String>> call = service.getTiposInventariable();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
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


    public MutableLiveData<List<Ticket>> getTicketsInventariable(String id_inventariable) {
        final MutableLiveData<List<Ticket>> data = new MutableLiveData<>();
        Call<List<Ticket>> call = service.getTicketsInventariable(id_inventariable);
        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Log.d("MENSAJE ERROR", ""+t.getMessage());
            }
        });
        return data;
    }


    public MutableLiveData<Inventariable> updateInventariable(String idInventariable, Inventariable inventariable_edit) {
        final MutableLiveData<Inventariable> data = new MutableLiveData<>();
        Call<Inventariable> update = service.updateInventariable(idInventariable, inventariable_edit);
        update.enqueue(new Callback<Inventariable>() {
            @Override
            public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                if (response.isSuccessful()) {
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

    public void deleteInventariable(String idInventariable) {
        Call<Inventariable> call = service.deleteInventariable(idInventariable);
        call.enqueue(new Callback<Inventariable>() {
            @Override
            public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                if (response.isSuccessful()) {
                    Log.e("DISPOSITIVO", "BORRADO");
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Inventariable> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    
    //ANOTACIONES

    public MutableLiveData<Anotacion> getAnotacion(String idAnotacion){
        final MutableLiveData<Anotacion> data = new MutableLiveData<>();
        Call<Anotacion> callAnotacion = anotacionService.getAnotacion(idAnotacion);
        callAnotacion.enqueue(new Callback<Anotacion>() {
            @Override
            public void onResponse(Call<Anotacion> call, Response<Anotacion> response) {
                if (response.isSuccessful()){
                    data.setValue(response.body());
                }
                Log.e("GET ANOTACION", ""+response);
            }

            @Override
            public void onFailure(Call<Anotacion> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
        return data;
    }


    public MutableLiveData<List<Anotacion>> getAnotacionesByTicket(String idTicket){
        final MutableLiveData<List<Anotacion>> data = new MutableLiveData<>();
        Call<List<Anotacion>> call = anotacionService.getAnotacionesTicket(idTicket);
        call.enqueue(new Callback<List<Anotacion>>() {
            @Override
            public void onResponse(Call<List<Anotacion>> call, Response<List<Anotacion>> response) {
                if(response.isSuccessful()){
                    Log.e("ANOTACIONES", "GUARADAS");
                    data.setValue(response.body());
                }
                Log.e("GET ANOTACIONES", ""+response);
            }

            @Override
            public void onFailure(Call<List<Anotacion>> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });

        return data;
    }

    public MutableLiveData<Anotacion> createAnotacion(CreateAnotacion createAnotacion){
        final MutableLiveData<Anotacion> data = new MutableLiveData<>();
        Call<Anotacion> createAnotacionCall = anotacionService.createAnotacion(createAnotacion);
        createAnotacionCall.enqueue(new Callback<Anotacion>() {
            @Override
            public void onResponse(Call<Anotacion> call, Response<Anotacion> response) {
                if(response.isSuccessful()){
                    Log.e("Nueva", "anotacion");
                    data.setValue(response.body());
                }
                Log.e("CREATE ANOTACION", ""+response);
            }

            @Override
            public void onFailure(Call<Anotacion> call, Throwable t) {
                Log.e("failure", ""+t.getMessage());
            }
        });
        return data;
    }

    public void deleteAnotacion(String idAnotacion) {
        Call<Void> call = anotacionService.deleteAnotacion(idAnotacion);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("ANOTACION", "BORRADA");
                }

                Log.e("DELETE ANOTACION", ""+response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("failure", ""+t.getMessage());
            }
        });
    }

    public MutableLiveData<Anotacion> updateAnotacion(String idAnotacion, CreateAnotacion anotacion){
        final MutableLiveData<Anotacion> data = new MutableLiveData<>();
        Call<Anotacion> call = anotacionService.updateAnotacion(idAnotacion, anotacion);
        call.enqueue(new Callback<Anotacion>() {
            @Override
            public void onResponse(Call<Anotacion> call, Response<Anotacion> response) {
                if(response.isSuccessful()){
                    Log.e("ANOTACION", "MODIFICADA");
                    data.setValue(response.body());
                }

                Log.e("UPDATE ANOTACION", ""+response);
            }

            @Override
            public void onFailure(Call<Anotacion> call, Throwable t) {
                Log.e("failure", ""+t.getMessage());
            }
        });
        return data;
    }
}
