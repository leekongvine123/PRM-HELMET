package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.HelmetAdapter;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.model.Helmet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Load HomeFragment by default
        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();

            }
         //   } else if (itemId == R.id.navigation_favorites) {
           //     selectedFragment = new FavoritesFragment();
            //} else if (itemId == R.id.navigation_cart) {
              //  selectedFragment = new CartFragment();
            //} else if (itemId == R.id.navigation_profile) {
             //   selectedFragment = new ProfileFragment();
            //}

            // Load the selected fragment
            return loadFragment(selectedFragment);
        });

    }
    private boolean loadFragment(Fragment fragment) {
        // Replace the current fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment); // Ensure you use the correct container ID
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }
}
