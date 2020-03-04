package com.gonzaloandcompany.satapp.ui.userdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.data.viewmodel.MyUserViewModel;
import com.gonzaloandcompany.satapp.data.viewmodel.UserViewModel;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PerfilDetailActivity extends AppCompatActivity {
    @BindView(R.id.TextViewEmail)
    TextView email;


    private MyUserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_detail);
        loadUser();
    }

    public void loadUser(){
        userViewModel.getMyUser().observe(this, new Observer<UsuarioDummy>() {
            @Override
            public void onChanged(UsuarioDummy usuario) {
                    setData(usuario);
                Log.d("ERROR", "ERROR"+usuario.getEmail());
            }
        });
    }
    public void setData(UsuarioDummy user) {
        Log.d("ERROR", user.getEmail());
        email.setText(user.getEmail());
    }

}
