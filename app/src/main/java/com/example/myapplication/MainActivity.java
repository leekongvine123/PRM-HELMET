package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.fragments.CartFragment;
import com.example.myapplication.fragments.FavoriteFragment;
import com.example.myapplication.fragments.HistoryFragment;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.MapFragment;
import com.example.myapplication.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Check if the user is logged in


        if (user != null) {
            // User is logged in, proceed with loading the HomeFragment
            loadFragment(new HomeFragment());
        } else {
            // User is not logged in, redirect to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity to prevent back navigation to it
        }
        // Load HomeFragment by default
        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_favorite) {
                selectedFragment = new MapFragment();
            } else if (itemId == R.id.navigation_cart) {
                selectedFragment = new CartFragment();
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            } else if (itemId == R.id.navigation_history) {
                selectedFragment = new HistoryFragment();
            }

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
