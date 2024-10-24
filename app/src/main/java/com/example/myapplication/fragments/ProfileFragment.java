package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private TextView nameValueTextView;
    private TextView emailValueTextView;
    private TextView phoneValueTextView;
    private TextView addressValueTextView;

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

        // Initialize UI elements
        nameValueTextView = view.findViewById(R.id.nameValueTextView);
        emailValueTextView = view.findViewById(R.id.emailValueTextView);
        phoneValueTextView = view.findViewById(R.id.phoneValueTextView);
        addressValueTextView = view.findViewById(R.id.addressValueTextView);

        loadUserData();

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
}
