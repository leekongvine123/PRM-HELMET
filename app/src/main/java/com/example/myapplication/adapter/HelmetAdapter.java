package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.HelmetDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Helmet;

import java.util.List;

public class HelmetAdapter extends RecyclerView.Adapter<HelmetAdapter.ViewHolder> {

    private List<Helmet> helmetList;
    private Context context;

    public HelmetAdapter(List<Helmet> helmetList, Context context) {
        this.helmetList = helmetList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helmet_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Helmet helmet = helmetList.get(position);
        holder.nameTextView.setText(helmet.getName());
        holder.brandTextView.setText(helmet.getBrand());
        holder.priceTextView.setText("$" + helmet.getPrice());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HelmetDetailActivity.class);
            intent.putExtra("productCode", helmet.getProductCode());
            intent.putExtra("helmetID", helmet.getHelmetID());
            intent.putExtra("helmetName", helmet.getName());
            intent.putExtra("helmetPrice", helmet.getPrice());
            intent.putExtra("helmetDescription", helmet.getDescription());
            intent.putExtra("helmetImageUrl", helmet.getImageUrl());
            context.startActivity(intent);
        });

        // Use Glide to load the image
        Glide.with(holder.itemView.getContext()).load(helmet.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return helmetList.size();
    }

    // Method to update the list of helmets (used for search functionality)
    public void updateList(List<Helmet> newList) {
        helmetList = newList;
        notifyDataSetChanged();  // Notify the adapter to refresh the RecyclerView
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView;
        public TextView brandTextView;
        public TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            brandTextView = itemView.findViewById(R.id.brandTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}
