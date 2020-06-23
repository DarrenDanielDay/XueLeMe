package com.example.xueleme;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Iterator;
import java.util.Map;

import FunctionPackge.Groupkey;
import interface_packge.ConnectionHandler;
import interface_packge.RequestHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = getIntent();
//        String users_info = intent.getStringExtra("extra_data");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        LoginActivity.users.setConnectionHandler(new ConnectionHandler() {
            @Override
        public void connectionSuccess() {
            Looper.prepare();
            Log.d("sfs","Success");
            Looper.loop();
        }

        @Override
        public void connectionFailed() {
            Looper.prepare();
            Log.d("sfs","F");
            Looper.loop();
        }
    });
        LoginActivity.users.IDquery();

}


    }


