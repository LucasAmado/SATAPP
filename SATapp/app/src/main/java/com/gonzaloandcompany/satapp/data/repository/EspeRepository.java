package com.gonzaloandcompany.satapp.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.requests.TicketCreateRequest;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.TicketService;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EspeRepository {
    TicketService service;
    String img;
    public EspeRepository() {
        this.service = ApiSAT.createServicePeticiones(TicketService.class, Constants.TOKEN_PROVISIONAL);
    }


    public LiveData<List<Ticket>> getTickets(int page, int limit) {
        final MutableLiveData<List<Ticket>> data = new MutableLiveData<>();
        Call<List<Ticket>> call = service.getTickets(page,limit);
        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {

            }
        });
        return data;
    }

    public LiveData<Ticket> getTicket(String id) {
        final MutableLiveData<Ticket> data = new MutableLiveData<>();
        Call<Ticket> call = service.getTicket(id);
        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {

            }
        });
        return data;
    }

    public LiveData<Ticket> createTicket(TicketCreateRequest request){
        final MutableLiveData<Ticket> data = new MutableLiveData<>();
        Log.d("REQUEST",request.toString());
        RequestBody title = RequestBody.create(request.getTitulo(), MultipartBody.FORM);
        RequestBody description = RequestBody.create(request.getDescripcion(),MultipartBody.FORM);
        RequestBody inventariable = RequestBody.create(request.getInventariable(),MultipartBody.FORM);
        RequestBody tech = RequestBody.create(request.getTecnico(), MultipartBody.FORM);

        Call<Ticket> call=null;

        if(request.getInventariable().isEmpty()&& request.getTecnico().isEmpty()){
            call = service.createTicket(title,description, null,null,request.getFotos());
        }else if(request.getInventariable().isEmpty()&&!request.getTecnico().isEmpty()){
          //TODO: DA 500 SI LE DOY UN TECNICO
            call = service.createTicket(title,description, null,tech,request.getFotos());
        }else if(!request.getInventariable().isEmpty()&&request.getTecnico().isEmpty()){
            call = service.createTicket(title,description, inventariable,null,request.getFotos());
        }else if(!request.getInventariable().isEmpty()&&!request.getTecnico().isEmpty()){
            call = service.createTicket(title,description, inventariable, tech,request.getFotos());
        }

        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                Log.d("RESPONSE",response.message().toString());
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                Log.d("CREATE TICKET","FAILURE: "+t.getMessage());
            }
        });
        return data;
    }

    public void deleteTicket(String id){
        Call<Void> call = service.deleteTicket(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("DELETE TICKET",id+" SUCCESSFUL");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("DELETE TICKET",id+" FAILURE");
            }

        });

    }

}
