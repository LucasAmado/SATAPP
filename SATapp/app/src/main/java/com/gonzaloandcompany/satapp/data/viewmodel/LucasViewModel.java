package com.gonzaloandcompany.satapp.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.data.repository.LucasRepository;

import java.util.List;

public class LucasViewModel extends AndroidViewModel{
    private LucasRepository repository;
    private MutableLiveData<List<String>> stringList;

    public LucasViewModel(@NonNull Application application) {
        super(application);
        repository = new LucasRepository();
    }

    public LiveData<List<String>> getAllTipos(){
        stringList = repository.getTipos();
        return stringList;
    }

    public LiveData<List<String>> getAllUbicaciones(){
        stringList = repository.getUbicaciones();
        return stringList;
    }
}