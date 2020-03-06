package com.gonzaloandcompany.satapp.data.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gonzaloandcompany.satapp.data.repository.UserRepository;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;

import java.util.List;

import lombok.NonNull;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository();
    }

    public LiveData<List<UsuarioDummy>> getUsers() {
        return repository.getUsers();
    }

    //TODO: CAMBIAR A MODELO USUARIO DE GONZALO
    public LiveData<List<UsuarioDummy>> getUsersPaginable(int page, int limit) {
        return repository.getUsersPaginable(page, limit);
    }
    public LiveData<UsuarioDummy> getUser(String id) {
        return repository.getUser(id);
    }

    public void deleteUser(String id){repository.deleteUser(id);}

    public LiveData<UsuarioDummy> promote(String id) {
        return repository.promote(id);
    }
    public LiveData<UsuarioDummy>  validate(String id) {
        return repository. validate(id);
    }
    public LiveData<UsuarioDummy>  getCurrentUser() {
        return repository.getCurrentUser();
    }
}
