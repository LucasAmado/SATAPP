package com.gonzaloandcompany.satapp.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.Register;
import com.gonzaloandcompany.satapp.retrofit.LoginService;
import com.gonzaloandcompany.satapp.retrofit.ServiceGeneratorLogin;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editTextNameRegistro)
    EditText nameRegistro;

    @BindView(R.id.editTextEmailRegistro)
    EditText emailRegistro;

    @BindView(R.id.editTextPasswordRegistro)
    EditText passwordRegistro;

    @BindView(R.id.buttonRegistrarse)
    Button registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = nameRegistro.getText().toString().trim();
                String email = emailRegistro.getText().toString().trim();
                String password = passwordRegistro.getText().toString().trim();

                Register registro = new Register(fullname, email, password);

                LoginService service = ServiceGeneratorLogin.createServiceToken(LoginService.class);
            }
        });

    }
}
