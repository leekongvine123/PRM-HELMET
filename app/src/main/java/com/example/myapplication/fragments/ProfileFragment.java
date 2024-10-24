package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private TextView nameValueTextView;
    private TextView emailValueTextView;
    private TextView phoneValueTextView;
    private TextView addressValueTextView;
    private Button logoutButton;

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    private DatabaseHelper databaseHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseHelper = new DatabaseHelper(context); // Initialize DatabaseHelper
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Initialize UI elements
        nameValueTextView = view.findViewById(R.id.nameValueTextView);
        emailValueTextView = view.findViewById(R.id.emailValueTextView);
        phoneValueTextView = view.findViewById(R.id.phoneValueTextView);
        addressValueTextView = view.findViewById(R.id.addressValueTextView);
        logoutButton = view.findViewById(R.id.logoutButton);

        loadUserData();
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void loadUserData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String email = firebaseUser.getEmail();

            User user = databaseHelper.getUserByEmail(email);

            if (user != null) {
                Log.d("ProfileFragment", "User Data: Name = " + user.getName() + ", Phone = " + user.getPhone());

                nameValueTextView.setText(user.getName());
                emailValueTextView.setText(user.getEmail());
                phoneValueTextView.setText(user.getPhone());
                addressValueTextView.setText(user.getAddress());
            } else {
                Toast.makeText(getActivity(), "User data not found in the database", Toast.LENGTH_SHORT).show();
                Log.e("ProfileFragment", "User data not found in the database");
            }
        } else {
            Toast.makeText(getActivity(), "No logged-in user", Toast.LENGTH_SHORT).show();
            Log.e("ProfileFragment", "FirebaseUser is null, no logged-in user.");
        }
    }

    private void logout() {
        // Firebase logout
        mAuth.signOut();

        // Google Sign-Out (if the user signed in using Google)
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Redirect to Login Activity after sign-out
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the backstack
                startActivity(intent);
                Toast.makeText(getActivity(), "Successfully logged out", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Logout failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
