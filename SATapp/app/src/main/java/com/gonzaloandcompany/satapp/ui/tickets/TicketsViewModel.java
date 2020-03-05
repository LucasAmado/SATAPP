package com.gonzaloandcompany.satapp.ui.tickets;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gonzaloandcompany.satapp.data.repository.EspeRepository;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.requests.TicketAssignRequest;
import com.gonzaloandcompany.satapp.requests.TicketCreateRequest;
import com.gonzaloandcompany.satapp.requests.TicketUpdateRequest;
import com.gonzaloandcompany.satapp.requests.TicketUpdateStateRequest;

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
        Log.d("VIEWMODEL TICKETS","TRUE");
        return repository.getTicketsAssigned(page, limit);
    }
    public LiveData<List<Ticket>> getTicketsCreated(int page, int limit) {
        return repository.getTicketsCreated(page, limit);
    }
    public LiveData<Ticket> assignTech(String id, TicketAssignRequest request) {
        return repository.assignTech(id, request);
    }
    public LiveData<Ticket> updateState(String id, TicketUpdateStateRequest request) {
        return repository.updateState(id, request);
    }

    public LiveData<Ticket> update(String id, TicketUpdateRequest request) {
        return repository.update(id, request);
    }
}