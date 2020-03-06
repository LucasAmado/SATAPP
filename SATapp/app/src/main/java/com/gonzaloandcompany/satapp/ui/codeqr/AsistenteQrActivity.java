package com.gonzaloandcompany.satapp.ui.codeqr;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.ui.home.detail.InventariableDetailActivity;

public class AsistenteQrActivity extends AppCompatActivity {
    String id_inventariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asiente_qr);

        Button btnEscanear = findViewById(R.id.btnEscanear);

        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AsistenteQrActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AsistenteQrActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISOS_CAMARA);
                } else {
                    escanear();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PERMISOS_CAMARA) {
            if (ContextCompat.checkSelfPermission(AsistenteQrActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                escanear();
            } else {
                Toast.makeText(this, "Sin los permisos de cámara no podemos escanear Qr", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void escanear() {
        Intent i = new Intent(AsistenteQrActivity.this, ActivityEscanear.class);
        startActivityForResult(i, Constants.CODIGO_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    id_inventariable = data.getStringExtra(Constants.RESULT_QR);
                    Intent intent = new Intent(AsistenteQrActivity.this, InventariableDetailActivity.class);
                    intent.putExtra(Constants.ID_INVENTARIABLE, id_inventariable);
                    startActivity(intent);
                }
            }
        }
    }
}
