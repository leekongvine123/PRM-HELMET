package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.model.CartItem;

import java.util.List;

public class CheckoutFragment extends Fragment {

    private RecyclerView checkoutRecyclerView;
    private TextView totalAmountTextView;
    private Button paypalButton;
    private List<CartItem> selectedItems;
    private CartAdapter checkoutAdapter;
    private ImageView backBtn;

    public CheckoutFragment(List<CartItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        checkoutRecyclerView = view.findViewById(R.id.checkoutRecyclerView);
        totalAmountTextView = view.findViewById(R.id.totalAmountTextView);
        paypalButton = view.findViewById(R.id.paypalButton);
        backBtn = view.findViewById(R.id.backButton);

        for (CartItem cart:
                selectedItems) {
            Log.d("CartItemData", "CartID: " + cart.getCartID() + ", HelmetName: " + cart.getHelmet().getName() + ", Size: " + cart.getHelmet().getSize() + ", Color: " + cart.getHelmet().getColor() + ", Quantity: " + cart.getQuantity() + ", Price: $" + cart.getPrice() + ", ImageURL: " + cart.getHelmet().getImageUrl());
        }


        if (selectedItems != null && !selectedItems.isEmpty()) {
            checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            checkoutAdapter = new CartAdapter(requireContext(), selectedItems);
            checkoutRecyclerView.setAdapter(checkoutAdapter);

            // Calculate total amount
            double totalAmount = calculateTotal(selectedItems);
            totalAmountTextView.setText("Total: $" + String.format("%.2f", totalAmount));
        } else {
            totalAmountTextView.setText("No items selected");
        }

        paypalButton.setOnClickListener(v -> {
            // Initiate PayPal payment process
        });

        backBtn.setOnClickListener(v-> back());

        return view;
    }

    private void back() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private double calculateTotal(List<CartItem> selectedItems) {
        double total = 0;
        for (CartItem item : selectedItems) {
            total += item.getPrice();
        }
        return total;
    }
}

