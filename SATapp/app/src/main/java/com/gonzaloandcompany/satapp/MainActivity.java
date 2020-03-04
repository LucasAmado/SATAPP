package com.gonzaloandcompany.satapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.data.viewmodel.JLuisViewModel;
import com.gonzaloandcompany.satapp.ui.home.detail.InventariableDetailActivity;
import com.gonzaloandcompany.satapp.ui.tickets.TicketListener;
import com.gonzaloandcompany.satapp.ui.ticketsdetail.TicketDetailActivity;
import com.gonzaloandcompany.satapp.ui.userdetail.PerfilDetailActivity;
import com.gonzaloandcompany.satapp.ui.userdetail.UserDetailActivity;
import com.gonzaloandcompany.satapp.ui.users.UserListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements TicketListener, UserListener {
    JLuisViewModel jLuisViewModel;

    @Override
    public void OnUserClick(String id) {
        Intent goToDetail = new Intent(this, UserDetailActivity.class);
        goToDetail.putExtra("userID",id);
        startActivity(goToDetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.perfilIcon) {
            Intent perfil = new Intent(this, PerfilDetailActivity.class);
            startActivity(perfil);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        jLuisViewModel = new ViewModelProvider(this).get(JLuisViewModel.class);

        jLuisViewModel.getIdInventariable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String idInventariable) {
                if (idInventariable != null) {
                    Intent i = new Intent(MainActivity.this, InventariableDetailActivity.class);
                    i.putExtra(Constants.ID_INVENTARIABLE, idInventariable);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    public void onTicketClick(String id) {
        Intent goToDetail = new Intent(this, TicketDetailActivity.class);
        goToDetail.putExtra("ticketID",id);
        startActivity(goToDetail);

    }


}
