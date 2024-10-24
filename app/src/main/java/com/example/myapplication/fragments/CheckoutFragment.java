package com.example.myapplication.fragments;
import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.CartItem;
import com.example.myapplication.model.Helmet;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderDetail;
import com.example.myapplication.model.Payment;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutFragment extends Fragment {

    private RecyclerView checkoutRecyclerView;
    private TextView totalAmountTextView;
    private Button paypalButton;
    private List<CartItem> selectedItems;
    private CartAdapter checkoutAdapter;
    private ImageView backBtn;
    private DatabaseHelper dbHelper;
    FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
    String clientId = "AXcM1ViW0yHvDxEks2rEp6JwtRk4Zrw9niI3hWGPRHKn967ROCIftzH3QDzVwzx87ihx9GTttcPfxaHp";
    int PAYPAL_REQUEST_CODE = 123;
    private int orderId = 0;
    User user ;
    public static PayPalConfiguration configuration;
    public CheckoutFragment(List<CartItem> selectedItems, DatabaseHelper dbHelper) {
        this.selectedItems = selectedItems;
        this.dbHelper = dbHelper;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // user
        // Check if the user is logged in
        configuration =  new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK).clientId(clientId);

         user = dbHelper.getUserByEmail(userFirebase.getEmail());


        checkoutRecyclerView = view.findViewById(R.id.checkoutRecyclerView);
        totalAmountTextView = view.findViewById(R.id.totalAmountTextView);
        paypalButton = view.findViewById(R.id.paypalButton);
        backBtn = view.findViewById(R.id.backButton);

        for (CartItem cart : selectedItems) {
            Log.d("CartItemData", "CartID: " + cart.getCartID() + ", HelmetName: " + cart.getHelmet().getName() +
                    ", Size: " + cart.getHelmet().getSize() + ", Color: " + cart.getHelmet().getColor() +
                    ", Quantity: " + cart.getQuantity() + ", Price: $" + cart.getPrice() +
                    ", ImageURL: " + cart.getHelmet().getImageUrl());
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
            getPayment();
        });

        backBtn.setOnClickListener(v -> back());

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

    private void getPayment() {
        if (selectedItems == null || selectedItems.isEmpty()) {
            Toast.makeText(requireContext(), "No items in the cart.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Step 1: Calculate the total amount from the selected cart items
        double totalAmount = calculateTotal(selectedItems);

        // Step 2: Create a description for the payment using the product names
        StringBuilder productNames = new StringBuilder();
        for (CartItem item : selectedItems) {
            productNames.append(item.getHelmet().getName()).append(", ");
        }

        // Remove the trailing comma and space
        if (productNames.length() > 0) {
            productNames.setLength(productNames.length() - 2);
        }

        // Convert totalAmount to a string and set the product description
        String amounts = String.format("%.2f", totalAmount);
        String productName = productNames.toString();

        for (CartItem item : selectedItems) {
            Helmet helmet = dbHelper.getHelmetById(item.getHelmetID());

            // Check if helmet stock is 0
            if (helmet.getStock() == 0) {
                Toast.makeText(requireContext(), "Helmet " + helmet.getName() + " is out of stock!", Toast.LENGTH_SHORT).show();
                return;  // Exit the loop if any helmet is out of stock
            }
        }


        // Step 3: Create an order with "Pending" status
        //
        Order newOrder = new Order(user.getId(), totalAmount, "Pending"); // Assuming userId is available

        try {
            orderId = dbHelper.addOrder(newOrder);

        }catch (Exception e)
        {

        }

        // Step 4: Create order details
        for (CartItem item : selectedItems) {
            OrderDetail orderDetail = new OrderDetail(orderId, item.getHelmetID(), item.getQuantity(), item.getPrice());
           dbHelper.addOrderDetail(orderDetail); // Add each order detail to the database
            Helmet helmet = dbHelper.getHelmetById(item.getHelmetID());
        }

        // Step 5: Create a PayPal payment
        Payment payment = new Payment(orderId, totalAmount, "PayPal"); // Modify this based on your Payment class
        int paymentId = dbHelper.addPayment(payment); // Store the new payment ID

        // Step 6: Initiate PayPal payment process
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amounts), "USD", productName, PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(requireActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        // Start the payment activity
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            // Retrieve the order ID from the intent if necessary

            if (resultCode == Activity.RESULT_OK && data != null) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {
                    try {
                        // Convert payment confirmation details to JSON for logging or further processing
                        String paymentDetails = paymentConfirmation.toJSONObject().toString();
                        JSONObject object = new JSONObject(paymentDetails);

                        // Log the payment details for debugging
                        Log.i("PaymentDetails", paymentDetails);

                        // Update order and payment status to "Success"
                        Order order = dbHelper.getOrderById(orderId); // Retrieve the order based on the orderId
                        if (order != null) {
                            order.setPaymentStatus("PAID");
                            dbHelper.updateOrder(order); // Update the order status in the database
                        }
                        List<OrderDetail> orderDetails = dbHelper.getOrderDetailsByOrderId(orderId);
                        for (OrderDetail item: orderDetails) {
                            Helmet helmet = dbHelper.getHelmetById(item.getHelmetID());
                            helmet.setStock(helmet.getStock()-1);
                            dbHelper.updateHelmet(helmet);
                        }

                        for (CartItem item : selectedItems) {
                         dbHelper.deleteCartItem(item.getCartID());
                        }

                        Toast.makeText(requireContext(), "Payment Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        startActivityForResult(intent,100);

                    } catch (JSONException e) {
                        Toast.makeText(requireContext(), "Error parsing payment details: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Update order and payment status to "Canceled"
                Order order = dbHelper.getOrderById(orderId); // Retrieve the order based on the orderId
                if (order != null) {
                    order.setPaymentStatus("CANCELED");
                    dbHelper.updateOrder(order); // Update the order status in the database
                }

                Toast.makeText(requireContext(), "Payment canceled.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // Update order and payment status to "Failed" due to invalid payment
                Order order = dbHelper.getOrderById(orderId); // Retrieve the order based on the orderId
                if (order != null) {
                    order.setPaymentStatus("FAILED");
                    dbHelper.updateOrder(order); // Update the order status in the database
                }

                Toast.makeText(requireContext(), "Invalid payment.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
