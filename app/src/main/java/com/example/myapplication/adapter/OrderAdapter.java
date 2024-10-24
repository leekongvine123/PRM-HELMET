package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderDetail;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private DatabaseHelper databaseHelper;

    public OrderAdapter(List<Order> orderList, DatabaseHelper databaseHelper) {
        this.orderList = orderList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtOrderId.setText("Order ID: " + order.getOrderID());
        holder.txtOrderDate.setText("Order Date: " + order.getOrderDate());
        holder.txtTotalAmount.setText("Total Amount: $" + order.getTotalAmount());
        holder.txtPaymentStatus.setText("Payment Status: " + order.getPaymentStatus());

        // Retrieve the order details for this order and set up the nested RecyclerView
        List<OrderDetail> orderDetails = databaseHelper.getOrderDetailsByOrderId(order.getOrderID());
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(orderDetails, databaseHelper);
        holder.recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerViewOrderDetails.setAdapter(orderDetailAdapter);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderDate, txtTotalAmount, txtPaymentStatus;
        RecyclerView recyclerViewOrderDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtTotalAmount = itemView.findViewById(R.id.txtTotalAmount);
            txtPaymentStatus = itemView.findViewById(R.id.txtPaymentStatus);
            recyclerViewOrderDetails = itemView.findViewById(R.id.recyclerViewOrderDetails);
        }
    }
}
