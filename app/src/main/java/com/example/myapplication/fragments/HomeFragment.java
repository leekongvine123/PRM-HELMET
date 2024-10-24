package com.example.myapplication.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.adapter.HelmetAdapter;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.Helmet;

import java.util.List;

public class HomeFragment extends Fragment {

    private EditText searchEditText;
    private RecyclerView recyclerView;
    private HelmetAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Helmet> helmetList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(requireContext()); // Use requireContext() for non-null context

        // Fetch helmet data
        helmetList = dbHelper.getHelmets();

        // Setup RecyclerView with GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new HelmetAdapter(helmetList, requireContext());
        recyclerView.setAdapter(adapter);

        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the helmet list when text is entered
                filterHelmets(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        return view;
    }

    // Method to filter helmets by name
    private void filterHelmets(String query) {
        List<Helmet> filteredList = dbHelper.searchHelmetsByName(query);
        adapter.updateList(filteredList);  // Update the adapter with the filtered list
    }
}
