package com.gonzaloandcompany.satapp.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.data.repository.LucasRepository;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.Ticket;

import java.util.List;

public class LucasViewModel extends AndroidViewModel{
    private LucasRepository repository;
    private MutableLiveData<List<String>> stringList;
    private MutableLiveData<List<Ticket>> ticketList;
    private MutableLiveData<Inventariable> inventariable;

    public LucasViewModel(@NonNull Application application) {
        super(application);
        repository = new LucasRepository();
    }

    public LiveData<Inventariable> getInventariable(String id){
        inventariable = repository.getInventariableById(id);
        return inventariable;
    }

    public LiveData<List<String>> getAllTipos(){
        stringList = repository.getTipos();
        return stringList;
    }

    public LiveData<List<String>> getAllUbicaciones(){
        stringList = repository.getUbicaciones();
        return stringList;
    }

    public LiveData<List<Ticket>> getTicketsInventariable(String id) {
        ticketList = repository.getTicketsInventariable(id);
        return ticketList;
    }
}