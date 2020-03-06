package com.gonzaloandcompany.satapp.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gonzaloandcompany.satapp.data.repository.LucasRepository;
import com.gonzaloandcompany.satapp.mymodels.Anotacion;
import com.gonzaloandcompany.satapp.requests.CreateAnotacion;

import java.util.List;

public class AnotacionViewModel extends AndroidViewModel {
    private LucasRepository repository;
    public static String idAnotationSelected;

    public AnotacionViewModel(@NonNull Application application) {
        super(application);
        repository = new LucasRepository();
    }

    public static String getIdAnotationSelected() {
        return idAnotationSelected;
    }

    public static void setIdAnotationSelected(String idAnotationSelected) {
       AnotacionViewModel.idAnotationSelected = idAnotationSelected;
    }

    public LiveData<Anotacion> getAnotacion(String id){
        return repository.getAnotacion(id);
    }

    public LiveData<Anotacion> createAnotacion(CreateAnotacion anotacion){
        return repository.createAnotacion(anotacion);
    }

    public LiveData<List<Anotacion>> getAnotacionesByTicket(String idTicket){
        return repository.getAnotacionesByTicket(idTicket);
    }

    public void deleteAnotacion(String idAnotacion){
        repository.deleteAnotacion(idAnotacion);
    }

    public LiveData<Anotacion> updateAnotacion(String id,CreateAnotacion anotacion){
        return repository.updateAnotacion(id, anotacion);
    }
}
