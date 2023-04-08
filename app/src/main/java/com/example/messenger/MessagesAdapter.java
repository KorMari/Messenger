package com.example.messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    private  static  final  int VIEW_TYPE_MY_MESSAGE = 100;
    private  static  final  int VIEW_TYPE_OTHER_MESSAGE = 101;
    private List<Message> messages = new ArrayList<>();
    private  String currentId;

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public MessagesAdapter(String currentId) {
        this.currentId = currentId;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        if (viewType == VIEW_TYPE_MY_MESSAGE){
            layoutResId = R.layout.my_message_item;
        }else {
            layoutResId = R.layout.other_message_item;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent,false);

        return new MessagesViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(message.getSenderId().equals(currentId)){
            return  VIEW_TYPE_MY_MESSAGE;
        }else {
            return VIEW_TYPE_OTHER_MESSAGE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textViewMessage.setText(message.getText());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMessage;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
