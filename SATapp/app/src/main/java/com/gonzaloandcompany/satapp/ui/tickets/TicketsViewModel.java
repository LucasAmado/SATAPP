package com.gonzaloandcompany.satapp.ui.tickets;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gonzaloandcompany.satapp.data.repository.EspeRepository;
import com.gonzaloandcompany.satapp.mymodels.Ticket;

import java.util.List;

import lombok.NonNull;

public class TicketsViewModel extends AndroidViewModel {
    private EspeRepository repository;
    private LiveData<List<Ticket>> tickets;
    private static int ticketSelected;

    public TicketsViewModel(@NonNull Application application){
        super(application);
        repository = new EspeRepository();
    }

    public LiveData<List<Ticket>> getTickets(int page){
        return repository.getTickets(page);
    }
}