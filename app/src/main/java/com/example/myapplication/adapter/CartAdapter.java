package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.CartItem;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private DatabaseHelper dbHelper;  // Add DatabaseHelper field

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private OnCartChangeListener onCartChangeListener;

    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.onCartChangeListener = listener;
    }

    public CartAdapter(Context context, List<CartItem> cartItems, DatabaseHelper dbHelper) {
        this.context = context;
        this.cartItems = cartItems;
        this.dbHelper = dbHelper;  // Initialize dbHelper
    }

    // ViewHolder class for the RecyclerView items
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, itemSizeTextView, itemColorTextView, itemQuantityTextView, itemPriceTextView;
        ImageView itemImageView;
        CheckBox itemCheckBox;
        TextView plusButton, minusButton;  // Add these buttons

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views from the item layout
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemSizeTextView = itemView.findViewById(R.id.itemSizeTextView);
            itemColorTextView = itemView.findViewById(R.id.itemColorTextView);
            itemQuantityTextView = itemView.findViewById(R.id.quantityTextView);  // Quantity TextView
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemCheckBox = itemView.findViewById(R.id.itemCheckBox);

            plusButton = itemView.findViewById(R.id.plusButton);  // Plus button
            minusButton = itemView.findViewById(R.id.minusButton);  // Minus button
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the cart_item_layout.xml file for each row in RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the cart item at the current position
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            String email = firebaseUser.getEmail();
            User user = dbHelper.getUserByEmail(email);

        CartItem cartItem = cartItems.get(position);

        // Bind data to the views
        holder.itemNameTextView.setText(cartItem.getHelmetName());
        holder.itemSizeTextView.setText("Size: " + cartItem.getSize());
        holder.itemColorTextView.setText("Color: " + cartItem.getColor());
        holder.itemPriceTextView.setText("$" + String.format("%.2f", cartItem.getPrice()));

        Glide.with(holder.itemView.getContext()).load(cartItem.getHelmet().getImageUrl()).into(holder.itemImageView);
        holder.itemCheckBox.setChecked(cartItem.isSelected());

        holder.itemCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartItem.setSelected(isChecked);
            if (onCartChangeListener != null) {
                onCartChangeListener.onCartChanged();
            }
        });

        // Set the quantity
        holder.itemQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));

        // Handle increment button
        holder.plusButton.setOnClickListener(v -> {
            int currentQuantity = cartItem.getQuantity();
            cartItem.setQuantity(currentQuantity + 1);
            holder.itemQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
            dbHelper.updateCartItemQuantity(cartItem.getHelmetID(), user.getId(), cartItem.getQuantity());

            if (onCartChangeListener != null) {
                onCartChangeListener.onCartChanged();
            }
        });

        // Handle decrement button
        holder.minusButton.setOnClickListener(v -> {
            int currentQuantity = cartItem.getQuantity();
            if (currentQuantity > 1) {
                cartItem.setQuantity(currentQuantity - 1);
                holder.itemQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
                dbHelper.updateCartItemQuantity(cartItem.getHelmetID(), user.getId(), cartItem.getQuantity());

                if (onCartChangeListener != null) {
                    onCartChangeListener.onCartChanged();
                }
            }
        });

        Log.d("CartItemData", "CartID: " + cartItem.getCartID() + ", HelmetName: " + cartItem.getHelmetName() + ", Size: " + cartItem.getSize() + ", Color: " + cartItem.getColor() + ", Quantity: " + cartItem.getQuantity() + ", Price: $" + cartItem.getPrice());
    }


    @Override
    public int getItemCount() {
        // Return the size of the cart items list
        return cartItems.size();
    }

    public void selectAllItems(boolean isSelected) {
        for (CartItem item : cartItems) {
            item.setSelected(isSelected);
        }
        notifyDataSetChanged();
    }

    public double calculateTotalAmount() {
        double total = 0;
        for (CartItem cartItem : cartItems) {
            if (cartItem.isSelected()) {
                total += cartItem.getPrice();
            }
        }
        return total;
    }

    public List<CartItem> getSelectedItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }
}
