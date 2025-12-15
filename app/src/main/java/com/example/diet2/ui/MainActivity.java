package com.example.diet2.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.diet2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            boolean showBottomNav =
                    destination.getId() == R.id.homeFragment
                            || destination.getId() == R.id.dashboardFragment
                            || destination.getId() == R.id.notificationsFragment
                            || destination.getId() == R.id.profileFragment;
            bottomNav.setVisibility(showBottomNav ? View.VISIBLE : View.GONE);
        });
    }
}
