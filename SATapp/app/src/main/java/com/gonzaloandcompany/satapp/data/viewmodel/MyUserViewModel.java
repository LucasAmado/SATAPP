package com.gonzaloandcompany.satapp.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.data.repository.UserRepository;
import com.gonzaloandcompany.satapp.requests.EditUsers;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.requests.Password;

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
    public void updateMyUser(String id, EditUsers nombre) {
       repository.updateUser(id,nombre);
    }

    public  void updatePassword(String email, String passwordOld, Password passwordNew, String id){
        repository.updatePassword(email,passwordOld,passwordNew,id);
    }

}
