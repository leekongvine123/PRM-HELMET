package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.AdminListChatAdapter;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private Button btnSend;
    private ChatAdapter chatAdapter;
    private AdminListChatAdapter adminListChatAdapter;
    private DatabaseHelper db;
    private List<Message> messageList = new ArrayList<>();
    private int userId;
    private int otherUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize DatabaseHelper and UI elements
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        TextView tvNoData = findViewById(R.id.tvNoData); // "No Data" TextView

        // Get the current Firebase user and their email
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = firebaseUser.getEmail();
        User user = db.getUserByEmail(email);
        userId = user.getId();

        // Get otherUserId from the intent extras if available
        otherUserId = getIntent().getIntExtra("otherUserId", -1);

        // Check if the user is an admin (user ID = 1)
        if (user.getId() == 1 && otherUserId == -1) {
            // Admin view, list all latest messages from each sender
            messageList = db.getLatestMessagesFromEachSender(userId);
            adminListChatAdapter = new AdminListChatAdapter(messageList, userId, db);
            recyclerView.setAdapter(adminListChatAdapter);

            // Hide message input fields for the admin view
            etMessage.setVisibility(View.GONE);
            btnSend.setVisibility(View.GONE);

            // Show "No Data" if there are no messages
            toggleNoDataMessage(messageList, recyclerView, tvNoData);

        } else {
            // Regular user view: Load messages with the admin or start a new conversation
            if (otherUserId == -1) {
                otherUserId = 1; // Default to admin ID if not set
            }

            messageList = db.getMessages(userId, otherUserId);

            // Fetch messages between the current user and the admin
            chatAdapter = new ChatAdapter(messageList, userId);
            recyclerView.setAdapter(chatAdapter);

            // Set up send button functionality
            btnSend.setOnClickListener(v -> {
                String messageText = etMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    // Insert the message into the database
                    db.insertMessage(userId, otherUserId, messageText);

                    // Add the new message to the list and update the adapter
                    messageList.add(new Message(0, userId, otherUserId, messageText, null));
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    etMessage.setText(""); // Clear input field
                    recyclerView.scrollToPosition(messageList.size() - 1); // Scroll to the latest message

                    // Update "No Data" visibility
                    toggleNoDataMessage(messageList, recyclerView, tvNoData);
                }
            });

            // Toggle "No Data" message for the initial load
            toggleNoDataMessage(messageList, recyclerView, tvNoData);
        }

        // Set the layout manager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Method to toggle "No Data" message visibility
    private void toggleNoDataMessage(List<Message> messages, RecyclerView recyclerView, TextView tvNoData) {
        if (messages.isEmpty()) {
            tvNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
