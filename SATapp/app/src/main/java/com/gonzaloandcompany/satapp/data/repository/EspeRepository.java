package com.gonzaloandcompany.satapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.TicketService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EspeRepository {
    TicketService service;
    String img;
    public EspeRepository() {
        this.service = ApiSAT.createServicePeticiones(TicketService.class, Constants.TOKEN_PROVISIONAL);
    }


    public LiveData<List<Ticket>> getTickets(int page) {
        final MutableLiveData<List<Ticket>> data = new MutableLiveData<>();
        Call<List<Ticket>> call = service.getTickets(page);
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

}
