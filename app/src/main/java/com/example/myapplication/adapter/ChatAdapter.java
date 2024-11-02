package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> messageList;
    private int currentUserId;

    public ChatAdapter(List<Message> messageList, int currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout senderMessageLayout;
        private TextView tvSenderMessage, tvSenderTimestamp;
        private LinearLayout meMessageLayout;
        private TextView tvMeMessage, tvMeTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageLayout = itemView.findViewById(R.id.senderMessageLayout);
            tvSenderMessage = itemView.findViewById(R.id.tvSenderMessage);
            tvSenderTimestamp = itemView.findViewById(R.id.tvSenderTimestamp);

            meMessageLayout = itemView.findViewById(R.id.meMessageLayout);
            tvMeMessage = itemView.findViewById(R.id.tvMeMessage);
            tvMeTimestamp = itemView.findViewById(R.id.tvMeTimestamp);
        }

        public void bind(Message message, boolean isCurrentUser) {
            if (isCurrentUser) {
                // Show "me" message layout and hide sender layout
                meMessageLayout.setVisibility(View.VISIBLE);
                senderMessageLayout.setVisibility(View.GONE);
                tvMeMessage.setText(message.getMessage());
                tvMeTimestamp.setText(message.getTimestamp());
            } else {
                // Show sender message layout and hide "me" layout
                senderMessageLayout.setVisibility(View.VISIBLE);
                meMessageLayout.setVisibility(View.GONE);
                tvSenderMessage.setText(message.getMessage());
                tvSenderTimestamp.setText(message.getTimestamp());
            }
        }
    }
}
