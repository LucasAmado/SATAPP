package com.gonzaloandcompany.satapp.ui.userdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
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
import com.gonzaloandcompany.satapp.data.repository.UserRepository;
import com.gonzaloandcompany.satapp.data.viewmodel.UserViewModel;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.ui.tickets.TicketsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailActivity extends AppCompatActivity {
    //TODO: CAMBIAR MODELO USUARIODUMMY POR EL DE GONZALO

    @BindView(R.id.user_detail_email)
    TextView email;
    @BindView(R.id.user_detail_name)
    TextView name;
    @BindView(R.id.user_detail_title_name)
    TextView titleName;
    @BindView(R.id.user_detail_rol)
    TextView rol;
    @BindView(R.id.user_detail_validate)
    FloatingActionButton validate;
    @BindView(R.id.user_detail_photo)
    ImageView photo;
    @BindView(R.id.userDetailDelete)
    FloatingActionButton delete;
    @BindView(R.id.userDetailPromoteToTech)
    FloatingActionButton promote;
    @BindView(R.id.userDetailState)
    TextView validated;
    @BindView(R.id.validate_layout)
    LinearLayout layout;
    String id;

    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        ButterKnife.bind(this);
        userViewModel= new ViewModelProvider(this).get(UserViewModel.class);
        id= getIntent().getStringExtra("userID");
        loadUser();

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.validate(id).observe(UserDetailActivity.this, new Observer<UsuarioDummy>() {
                    @Override
                    public void onChanged(UsuarioDummy usuario) {
                        setData(usuario);
                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.deleteUser(id);
                finish();
            }
        });

        promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.promote(id).observe(UserDetailActivity.this, new Observer<UsuarioDummy>() {
                    @Override
                    public void onChanged(UsuarioDummy usuario) {
                        setData(usuario);
                    }
                });
            }
        });


    }

    public void loadUser(){


        userViewModel.getUser(id).observe(this, new Observer<UsuarioDummy>() {
            @Override
            public void onChanged(UsuarioDummy usuario) {
                if(usuario!=null)
                    setData(usuario);
                else
                    Toast.makeText(UserDetailActivity.this, "No se ha podido cargar los datos.\n\nContacte con el servicio técnico", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void setData(UsuarioDummy user){
        if(user.getName()!=null)
            name.setText(user.getName());
        else{
            titleName.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
        }

        if(rol.equals("tecnico"))
            rol.setText("Técnico");
        else if (rol.equals("user"))
            rol.setText("Usuario");
        else
            rol.setText("Administrador");

        email.setText(user.getEmail());

        if(user.isValidated()){
            layout.setVisibility(View.GONE);
            validated.setText("Validado");
        }else
            validated.setText("Pendiente de validar");

        if(user.getPicture()!=null){
            GlideUrl glideUrl = new GlideUrl(Constants.BASE_URL + user.getPicture(),
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + Constants.TOKEN_PROVISIONAL)
                            .build());

            Glide.with(UserDetailActivity.this).load(glideUrl).circleCrop().into(photo);
        }else{
            Glide.with(UserDetailActivity.this).load(R.drawable.iconfinder_unknown_403017).circleCrop().into(photo);
        }
    }
}
