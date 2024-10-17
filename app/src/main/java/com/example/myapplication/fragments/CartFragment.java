package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(requireContext());

        List<CartItem> cartItems = dbHelper.getCartItemsByUser(1);

        for (CartItem cart:
        cartItems) {
            Log.d("CartItemData", "CartID: " + cart.getCartID() + ", HelmetName: " + cart.getHelmet().getName() + ", Size: " + cart.getHelmet().getSize() + ", Color: " + cart.getHelmet().getColor() + ", Quantity: " + cart.getQuantity() + ", Price: $" + cart.getPrice() + ", ImageURL: " + cart.getHelmet().getImageUrl());
        }

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        CartAdapter cartAdapter = new CartAdapter(requireContext(), cartItems);
        cartRecyclerView.setAdapter(cartAdapter);

        queryUsers(dbHelper);
        queryHelmets(dbHelper);
        queryCartItems(dbHelper);

//        queryCartItemsByUserID(dbHelper);

        return view;
    }


    // Method to query and log user data
    private void queryUsers(DatabaseHelper dbHelper) {
        Cursor cursor = dbHelper.getAllUsers();

        if (cursor.moveToFirst()) {
            do {
                // Fetch user data
                @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex("Id"));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("Name"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("Email"));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("Phone"));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("Address"));

                // Log user data
                Log.d("UserData", "UserID: " + userId + ", Username: " + username + ", Email: " + email + ", Phone: " + phone + ", Address: " + address);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    // Method to query and log cart items
    private void queryCartItems(DatabaseHelper dbHelper) {
        Cursor cursor = dbHelper.getAllCartItemsView();

        if (cursor.moveToFirst()) {
            do {
                // Fetch cart item data
                @SuppressLint("Range") int cartId = cursor.getInt(cursor.getColumnIndex("CartID"));
                @SuppressLint("Range") String helmetName = cursor.getString(cursor.getColumnIndex("HelmetName"));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex("Size"));
                @SuppressLint("Range") String color = cursor.getString(cursor.getColumnIndex("Color"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("Price"));

                // Log cart item data
                Log.d("CartItemData", "CartID: " + cartId + ", HelmetName: " + helmetName + ", Size: " + size + ", Color: " + color + ", Quantity: " + quantity + ", Price: $" + price);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    // Method to query and log cart items
    private void queryCartItemsByUserID(DatabaseHelper dbHelper) {
        Cursor cursor = dbHelper.getAllCartItemsByUserId(1);

        if (cursor.moveToFirst()) {
            do {
                // Fetch cart item data
                @SuppressLint("Range") int cartId = cursor.getInt(cursor.getColumnIndex("CartID"));
                @SuppressLint("Range") String helmetName = cursor.getString(cursor.getColumnIndex("HelmetName"));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex("Size"));
                @SuppressLint("Range") String color = cursor.getString(cursor.getColumnIndex("Color"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("Price"));

                // Log cart item data
                Log.d("CartItemData", "CartID: " + cartId + ", HelmetName: " + helmetName + ", Size: " + size + ", Color: " + color + ", Quantity: " + quantity + ", Price: $" + price);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void queryHelmets(DatabaseHelper dbHelper) {
        Cursor cursor = dbHelper.getAllHelmets();

        if (cursor.moveToFirst()) {
            do {
                // Fetch helmet data
                @SuppressLint("Range") int helmetId = cursor.getInt(cursor.getColumnIndex("HelmetID"));
                @SuppressLint("Range") String helmetName = cursor.getString(cursor.getColumnIndex("Name"));
                @SuppressLint("Range") String productCode = cursor.getString(cursor.getColumnIndex("ProductCode"));
                @SuppressLint("Range") String brand = cursor.getString(cursor.getColumnIndex("Brand"));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex("Size"));
                @SuppressLint("Range") String color = cursor.getString(cursor.getColumnIndex("Color"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("Description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("Price"));
                @SuppressLint("Range") int stock = cursor.getInt(cursor.getColumnIndex("Stock"));
                @SuppressLint("Range") String imageUrl = cursor.getString(cursor.getColumnIndex("ImageURL"));
                @SuppressLint("Range") String createdAt = cursor.getString(cursor.getColumnIndex("CreatedAt"));

                // Log helmet data
                Log.d("HelmetData", "HelmetID: " + helmetId + ", Name: " + helmetName + ", ProductCode: " + productCode +
                        ", Brand: " + brand + ", Size: " + size + ", Color: " + color +
                        ", Description: " + description + ", Price: $" + price + ", Stock: " + stock +
                        ", ImageURL: " + imageUrl + ", CreatedAt: " + createdAt);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }



}
