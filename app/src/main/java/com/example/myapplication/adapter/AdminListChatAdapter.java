package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatActivity;
import com.example.myapplication.R;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.Message;

import java.util.List;

public class AdminListChatAdapter extends RecyclerView.Adapter<AdminListChatAdapter.ViewHolder> {
    private List<Message> messageList;
    private int currentUserId;
    private static DatabaseHelper dbHelper;
    private Context context;

    public AdminListChatAdapter(List<Message> messageList, int currentUserId, DatabaseHelper dbHelper) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        AdminListChatAdapter.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_chat, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message, message.getSenderId() == currentUserId);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtEmail;
        private TextView txtCurrentMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtCurrentMessage = itemView.findViewById(R.id.txtCurrentMessage);

            // Set up click listener to open ChatActivity with the selected user
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Message message = messageList.get(position);
                    int otherUserId = message.getSenderId();

                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("otherUserId", otherUserId);
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Message message, boolean isCurrentUser) {
            txtEmail.setText(dbHelper.getUserById(message.getSenderId()).getEmail());
            txtCurrentMessage.setText(message.getMessage());
        }
    }


}
