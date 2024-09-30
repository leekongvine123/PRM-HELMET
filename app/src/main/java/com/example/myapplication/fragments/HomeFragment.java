package com.example.myapplication.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.HelmetAdapter;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.Helmet;

import java.util.List;

public class HomeFragment extends Fragment {

    private EditText searchEditText;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Initialize DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext()); // Use requireContext() for non-null context

        // Clear any existing helmets and insert sample data
        dbHelper.deleteAllHelmets();
        dbHelper.insertSampleHelmets(); // Insert sample data into the database

        // Fetch helmet data
        List<Helmet> helmetList = dbHelper.getHelmets();

        // Setup RecyclerView with GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);
        HelmetAdapter adapter = new HelmetAdapter(helmetList,requireContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
