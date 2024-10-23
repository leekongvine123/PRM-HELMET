package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.DnsResolver;
import android.os.Bundle;
import android.telecom.Call;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.CartItem;
import com.google.android.gms.common.api.Response;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private DatabaseHelper dbHelper;
    private Button selectAllButton;
    private boolean isAllSelected = false;
    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;
    private Button checkoutButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        selectAllButton = view.findViewById(R.id.selectAllButton);
        TextView totalAmountTextView = view.findViewById(R.id.totalAmount);
        checkoutButton = view.findViewById(R.id.checkoutButton);

        dbHelper = new DatabaseHelper(requireContext());

        cartItems = dbHelper.getCartItemsByUser(1);

        for (CartItem cart:
        cartItems) {
            Log.d("CartItemData", "CartID: " + cart.getCartID() + ", HelmetName: " + cart.getHelmet().getName() + ", Size: " + cart.getHelmet().getSize() + ", Color: " + cart.getHelmet().getColor() + ", Quantity: " + cart.getQuantity() + ", Price: $" + cart.getPrice() + ", ImageURL: " + cart.getHelmet().getImageUrl());
        }

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartAdapter = new CartAdapter(requireContext(), cartItems);
        cartRecyclerView.setAdapter(cartAdapter);

        cartAdapter.setOnCartChangeListener(() -> updateTotalAmount(cartAdapter, totalAmountTextView));

        selectAllButton.setOnClickListener(v -> {
            if(isAllSelected) {
                cartAdapter.selectAllItems(false);
                selectAllButton.setText("Select All");
            } else {
                cartAdapter.selectAllItems(true);
                selectAllButton.setText("Deselect All");
            }
            isAllSelected = !isAllSelected;
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                showDeleteConfirmationDialog(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                if (dX < 0) {
                    // Swipe Left (Delete)
                    paint.setColor(Color.RED);
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(40);
                    c.drawText("Delete", (float) itemView.getRight() - 200, (float) itemView.getTop() + (itemView.getHeight() / 2), paint);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(cartRecyclerView);

        checkoutButton.setOnClickListener(v -> {
            List<CartItem> selectedItems = cartAdapter.getSelectedItems();

            if (selectedItems.isEmpty()) {
                Toast.makeText(requireContext(), "No items selected for checkout", Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = new Bundle();
                CheckoutFragment checkoutFragment = new CheckoutFragment(selectedItems,dbHelper);
                checkoutFragment.setArguments(bundle);

                // Assuming you are using Fragment transactions to navigate
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, checkoutFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        queryUsers(dbHelper);
        queryHelmets(dbHelper);
        queryCartItems(dbHelper);

        return view;
    }

    private void updateTotalAmount(CartAdapter cartAdapter, TextView totalAmountTextView) {
        double total = cartAdapter.calculateTotalAmount();
        totalAmountTextView.setText("Total: $" + String.format("%.2f", total));
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(requireContext())  // Use requireContext() instead of 'this'
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> deleteItem(position))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    cartAdapter.notifyItemChanged(position);
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteItem(int position) {
        if (position < 0 || position >= cartItems.size()) {
            return;
        }
        CartItem itemToDelete = cartItems.get(position);
        cartItems.remove(position);
        cartAdapter.notifyItemRemoved(position);
        dbHelper.removeItemFromCart(itemToDelete.getCartID());
    }

    // Method to query and log user data
    private void queryUsers(DatabaseHelper dbHelper) {
        Cursor cursor = dbHelper.getAllUsers();

        if (cursor.moveToFirst()) {
            do {
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
