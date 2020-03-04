package com.gonzaloandcompany.satapp.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.data.repository.JLuisRepository;
import com.gonzaloandcompany.satapp.data.repository.UserRepository;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;

import java.util.List;

public class MyUserViewModel extends AndroidViewModel {
    LiveData<UsuarioDummy> user;
    UserRepository repository;

    public MyUserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository();
        user = new MutableLiveData<>();
    }

    public LiveData<UsuarioDummy> getMyUser() {
        user = repository.getMyUser();
        return user;

    }
}
