package com.gonzaloandcompany.satapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.data.viewmodel.JLuisViewModel;
import com.gonzaloandcompany.satapp.data.viewmodel.UserViewModel;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.ui.home.detail.InventariableDetailActivity;
import com.gonzaloandcompany.satapp.ui.tickets.TicketListener;
import com.gonzaloandcompany.satapp.ui.ticketsdetail.TicketDetailActivity;
import com.gonzaloandcompany.satapp.ui.userdetail.PerfilDetailActivity;
import com.gonzaloandcompany.satapp.ui.userdetail.UserDetailActivity;
import com.gonzaloandcompany.satapp.ui.users.UserListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.gonzaloandcompany.satapp.common.MyApp.CHANNEL;

public class MainActivity extends AppCompatActivity implements TicketListener, UserListener {
    JLuisViewModel jLuisViewModel;
    private UserViewModel userViewModel;
    private UsuarioDummy currentUser;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void OnUserClick(String id) {
        Intent goToDetail = new Intent(this, UserDetailActivity.class);
        goToDetail.putExtra("userID", id);
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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);

        getCurrentUser(navController);

        NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, appBarConfiguration);


        jLuisViewModel = new ViewModelProvider(MainActivity.this).get(JLuisViewModel.class);

        jLuisViewModel.getIdInventariable().observe(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String idInventariable) {
                if (idInventariable != null) {
                    Intent i = new Intent(MainActivity.this, InventariableDetailActivity.class);
                    i.putExtra(Constants.ID_INVENTARIABLE, idInventariable);
                    startActivity(i);
                }
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


    }

    @Override
    public void onTicketClick(String id) {
        Intent goToDetail = new Intent(this, TicketDetailActivity.class);
        goToDetail.putExtra("ticketID", id);
        startActivity(goToDetail);

    }

    public void getCurrentUser(NavController navController) {
        userViewModel.getCurrentUser().observe(this, new Observer<UsuarioDummy>() {
            @Override
            public void onChanged(UsuarioDummy usuario) {
                currentUser = usuario;

                BottomNavigationView navAdmin = findViewById(R.id.nav_view);
                BottomNavigationView navOtherRol = findViewById(R.id.nav_view2);

                if (currentUser.getRole().equals("admin")) {
                    navAdmin.setVisibility(View.VISIBLE);
                    navOtherRol.setVisibility(View.INVISIBLE);
                    NavigationUI.setupWithNavController(navAdmin, navController);
                    OnChannel();
                } else {
                    navAdmin.setVisibility(View.INVISIBLE);
                    navOtherRol.setVisibility(View.VISIBLE);
                    NavigationUI.setupWithNavController(navOtherRol, navController);
                }

            }
        });
    }

    public void OnChannel() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL)
                .setSmallIcon(R.drawable.ic_play_icon)
                .setContentTitle("USUARIOS SIN VALIDAR")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(1,notification);

    }

}
