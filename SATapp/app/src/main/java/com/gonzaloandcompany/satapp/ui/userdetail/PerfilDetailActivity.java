package com.gonzaloandcompany.satapp.ui.userdetail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.gonzaloandcompany.satapp.data.viewmodel.MyUserViewModel;
import com.gonzaloandcompany.satapp.data.viewmodel.UserViewModel;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.retrofit.UserService;
import com.gonzaloandcompany.satapp.ui.home.detail.InventariableDetailActivity;
import com.gonzaloandcompany.satapp.ui.home.detail.InventariableDetaileImageActivity;
import com.gonzaloandcompany.satapp.ui.home.detail.InventariableDialogFragment;
import com.gonzaloandcompany.satapp.ui.tickets.TicketsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PerfilDetailActivity extends AppCompatActivity {
    //TODO: CAMBIAR MODELO USUARIODUMMY POR EL DE GONZALO

    @BindView(R.id.user_detail_email)
    TextView email;
    @BindView(R.id.user_detail_name)
    TextView name;
    @BindView(R.id.user_detail_title_name)
    TextView titleName;
    @BindView(R.id.user_detail_rol)
    TextView rol;
    @BindView(R.id.user_detail_photo)
    ImageView photo;
    @BindView(R.id.userDetailDelete)
    FloatingActionButton cambiarfoto;
    @BindView(R.id.userDetailPromoteToTech)
    FloatingActionButton editar;
    @BindView(R.id.buttonContraseña)
    Button password;
    String id;
    String nameUser;


    private MyUserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_detail);

        ButterKnife.bind(this);
        userViewModel= new ViewModelProvider(this).get(MyUserViewModel.class);
        loadUser();

        editar.setOnClickListener(v -> {
            DialogFragment dialog = new MyUserEditDialogFragment(id);
            dialog.show(getSupportFragmentManager(), "MyUserEditDialogFragment");
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new PasswordDialogFragment(id);
                dialog.show(getSupportFragmentManager(), "PasswordDialogFragment");
            }
        });

        cambiarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PerfilDetailActivity.this, MyUserDetailImagenActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });


    }

    public void loadUser(){

        userViewModel.getMyUser().observe(this, new Observer<UsuarioDummy>() {
            @Override
            public void onChanged(UsuarioDummy usuario) {
                if(usuario!=null){
                    setData(usuario);
                }else
                    Toast.makeText(PerfilDetailActivity.this, "No se ha podido cargar los datos.\n\nContacte con el servicio técnico", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setData(UsuarioDummy user){
        id = user.getId();
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
        }else if (rol.equals("user"))
            rol.setText("Usuario");
        else
            rol.setText("Administrador");

        rol.setVisibility(View.VISIBLE);
        email.setText(user.getEmail());
        email.setVisibility(View.VISIBLE);


        if(user.getPicture()!=null){
            GlideUrl glideUrl = new GlideUrl(Constants.BASE_URL + user.getPicture(),
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + Constants.TOKEN_PROVISIONAL)
                            .build());

            Glide.with(PerfilDetailActivity.this).load(glideUrl).into(photo);
        }else{
            Glide.with(PerfilDetailActivity.this).load(R.drawable.iconfinder_unknown_403017).circleCrop().into(photo);
        }
    }


}
