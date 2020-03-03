package com.gonzaloandcompany.satapp.ui.users;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gonzaloandcompany.satapp.data.repository.EspeRepository;
import com.gonzaloandcompany.satapp.data.repository.UserRepository;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.requests.TicketCreateRequest;

import java.util.List;

import lombok.NonNull;

public class UsersViewModel extends AndroidViewModel {
    private UserRepository repository;

    public UsersViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository();
    }
    //TODO: CAMBIAR A MODELO USUARIO DE GONZALO
    public LiveData<List<UsuarioDummy>> getUsersPaginable(int page, int limit) {
        return repository.getUsersPaginable(page, limit);
    }
}