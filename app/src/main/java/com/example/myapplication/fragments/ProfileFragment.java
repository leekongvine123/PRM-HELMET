package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView addressTextView;

    private DatabaseHelper databaseHelper;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseHelper = new DatabaseHelper(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        phoneTextView = view.findViewById(R.id.phoneTextView);
        addressTextView = view.findViewById(R.id.addressTextView);

        loadUserData();

        return view;
    }

    private void loadUserData() {
        // Get current user data from database
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        User user = databaseHelper.getUserByEmail(user1.getEmail());
        if (user != null) {
            nameTextView.setText("Name: "+user.getName());
            emailTextView.setText("Email: "+user.getEmail());
            phoneTextView.setText("Phone: "+user.getPhone());
            addressTextView.setText("Address: "+user.getAddress());
        } else {
            // Handle case where user data is not found
            // For example, show an error message or prompt user to log in again
        }
    }
}
