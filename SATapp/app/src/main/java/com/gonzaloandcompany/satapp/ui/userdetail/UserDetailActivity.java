package com.gonzaloandcompany.satapp.ui.userdetail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
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
    @BindView(R.id.promote_layout)
    LinearLayout promoteLayout;
    String id;
    String nameUser;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id2) {
                        userViewModel.deleteUser(id);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.setMessage("¿Estás seguro de que quieres eliminar a "+nameUser+" ?");
                builder.setTitle(R.string.app_name);
                builder.show();
            }
        });

        promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.promote(id).observe(UserDetailActivity.this, new Observer<UsuarioDummy>() {
                    @Override
                    public void onChanged(UsuarioDummy usuario) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id2) {
                                userViewModel.validate(id).observe(UserDetailActivity.this, new Observer<UsuarioDummy>() {
                                    @Override
                                    public void onChanged(UsuarioDummy usuario) {
                                        setData(usuario);
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("No", null);

                        builder.setMessage("¿Estás seguro de que quieres promocionar a "+nameUser+" a técnico? ");
                        builder.setTitle(R.string.app_name);
                        builder.show();

                    }
                });
            }
        });


    }

    public void loadUser(){

        userViewModel.getUser(id).observe(this, new Observer<UsuarioDummy>() {
            @Override
            public void onChanged(UsuarioDummy usuario) {
                if(usuario!=null){
                    Log.d("USUARIO",usuario.toString());
                    setData(usuario);
                }else
                    Toast.makeText(UserDetailActivity.this, "No se ha podido cargar los datos.\n\nContacte con el servicio técnico", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void setData(UsuarioDummy user){

        if(user.getName()!=null){
            nameUser=user.getName();
            name.setText(user.getName());
            name.setVisibility(View.VISIBLE);
        }else{
            titleName.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            nameUser=user.getEmail();
        }


        if(rol.equals("tecnico")) {
            rol.setText("Técnico");
            promoteLayout.setVisibility(View.GONE);
        }else if (rol.equals("user"))
            rol.setText("Usuario");
        else
            rol.setText("Administrador");

        rol.setVisibility(View.VISIBLE);
        email.setText(user.getEmail());
        email.setVisibility(View.VISIBLE);


        if(user.isValidated()){
            layout.setVisibility(View.GONE);
            validated.setText("Validado");
            promoteLayout.setVisibility(View.VISIBLE);
        }else{
            layout.setVisibility(View.VISIBLE);
            validated.setText("Pendiente de validar");
            promoteLayout.setVisibility(View.GONE);
        }
        validated.setVisibility(View.VISIBLE);


        if(user.getPicture()!=null){
            GlideUrl glideUrl = new GlideUrl(Constants.BASE_URL + user.getPicture(),
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + Constants.TOKEN_PROVISIONAL)
                            .build());

            Glide.with(UserDetailActivity.this).load(glideUrl).centerCrop().into(photo);
        }else{
            Glide.with(UserDetailActivity.this).load(R.drawable.iconfinder_unknown_403017).circleCrop().into(photo);
        }
    }


}
