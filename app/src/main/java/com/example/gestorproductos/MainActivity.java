package com.example.gestorproductos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gestorproductos.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private XFit xFitFragment;
    private SportHouse sportHouseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        xFitFragment = new XFit();
        sportHouseFragment = new SportHouse();

        // Cargamos XFit por defecto
        if (savedInstanceState == null) {
            cargarFragment(xFitFragment);
            binding.bottomNavigation.setSelectedItemId(R.id.nav_xfit);
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_xfit) {
                cargarFragment(xFitFragment);
                return true;
            } else if (id == R.id.nav_sporthouse) {
                cargarFragment(sportHouseFragment);
                return true;
            }
            return false;
        });
    }

    private void cargarFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}