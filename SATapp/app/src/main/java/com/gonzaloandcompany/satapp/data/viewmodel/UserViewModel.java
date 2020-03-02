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



}
