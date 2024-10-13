package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> cartItems;

    // Constructor
    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    // ViewHolder class for the RecyclerView items
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, itemSizeTextView, itemColorTextView, itemQuantityTextView, itemPriceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views from the item layout
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemSizeTextView = itemView.findViewById(R.id.itemSizeTextView);
            itemColorTextView = itemView.findViewById(R.id.itemColorTextView);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantityTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
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
        CartItem cartItem = cartItems.get(position);

        // Bind data to the views
        holder.itemNameTextView.setText(cartItem.getHelmetName());
        holder.itemSizeTextView.setText("Size: " + cartItem.getSize());
        holder.itemColorTextView.setText("Color: " + cartItem.getColor());
        holder.itemQuantityTextView.setText("Qty: " + cartItem.getQuantity());
//        holder.itemPriceTextView.setText("$" + String.format("%.2f", cartItem.getPrice()));

        Log.d("CartItemData", "CartID: " + cartItem.getCartID() + ", HelmetName: " + cartItem.getHelmetName() + ", Size: " + cartItem.getSize() + ", Color: " + cartItem.getColor() + ", Quantity: " + cartItem.getQuantity() + ", Price: $" + cartItem.getPrice());

    }

    @Override
    public int getItemCount() {
        // Return the size of the cart items list
        return cartItems.size();
    }
}
