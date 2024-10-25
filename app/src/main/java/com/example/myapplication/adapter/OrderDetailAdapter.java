package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.Helmet;
import com.example.myapplication.model.OrderDetail;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<OrderDetail> orderDetailList;
    private DatabaseHelper databaseHelper;

    public OrderDetailAdapter(List<OrderDetail> orderDetailList, DatabaseHelper databaseHelper) {
        this.orderDetailList = orderDetailList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);

        // Use getHelmetById to retrieve the helmet details
        Helmet helmet = databaseHelper.getHelmetById(orderDetail.getHelmetID());
        if (helmet != null) {
            holder.txtHelmetName.setText(helmet.getName());
            holder.txtHelmetSize.setText("Size: " + helmet.getSize());
            holder.txtHelmetColor.setText("Color: " + helmet.getColor());
            holder.txtPrice.setText("Price: $" + orderDetail.getPrice());
            holder.txtQuantity.setText("x" + orderDetail.getQuantity());

            // Load helmet image using Glide or any other image loading library
            Glide.with(holder.itemView.getContext())
                    .load(helmet.getImageUrl())
                    .into(holder.imgHelmet);
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView txtHelmetName, txtHelmetSize, txtHelmetColor, txtPrice, txtQuantity;
        ImageView imgHelmet;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHelmetName = itemView.findViewById(R.id.txtHelmetName);
            txtHelmetSize = itemView.findViewById(R.id.txtHelmetSize);
            txtHelmetColor = itemView.findViewById(R.id.txtHelmetColor);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            imgHelmet = itemView.findViewById(R.id.imgHelmet);
        }
    }
}
