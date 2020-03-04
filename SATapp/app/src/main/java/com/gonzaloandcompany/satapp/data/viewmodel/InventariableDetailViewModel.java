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

public class InventariableDetailViewModel extends AndroidViewModel {
    private LucasRepository repository;
    private MutableLiveData<List<Ticket>> ticketList;

    public InventariableDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new LucasRepository();
    }

    public void deleteInventariable(String id){
        repository.deleteInventariable(id);
    }

    public LiveData<List<Ticket>> getTicketsInventariable(String id) {
        return repository.getTicketsInventariable(id);
    }
}
