package com.gonzaloandcompany.satapp.ui.tickets;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gonzaloandcompany.satapp.data.repository.EspeRepository;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.requests.TicketCreateRequest;

import java.util.List;

import lombok.NonNull;

public class TicketsViewModel extends AndroidViewModel {
    private EspeRepository repository;

    public TicketsViewModel(@NonNull Application application) {
        super(application);
        repository = new EspeRepository();
    }

    public LiveData<List<Ticket>> getTickets(int page, int limit) {
        return repository.getTickets(page, limit);
    }

    public LiveData<Ticket> getTicket(String id) {
        return repository.getTicket(id);
    }

    public LiveData<Ticket> createTicket(TicketCreateRequest request){
        return repository.createTicket(request);
    }

    public void deleteTicket(String id){repository.deleteTicket(id);}

    public LiveData<List<Ticket>> getTicketsAssigned(int page, int limit) {
        return repository.getTicketsAssigned(page, limit);
    }
    public LiveData<List<Ticket>> getTicketsCreated(int page, int limit) {
        return repository.getTicketsCreated(page, limit);
    }
}