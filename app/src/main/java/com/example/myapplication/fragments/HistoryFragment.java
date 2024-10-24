package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private DatabaseHelper databaseHelper;

    public HistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = firebaseUser.getEmail();
        User user = databaseHelper.getUserByEmail(email);

        List<Order> orderList = getOrdersByUserId(user.getId());

        OrderAdapter orderAdapter = new OrderAdapter(orderList, databaseHelper);
        recyclerViewOrders.setAdapter(orderAdapter);

        return view;
    }

    private List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        orders = databaseHelper.getOrdersByUserId(userId);
        return orders;
    }
}
