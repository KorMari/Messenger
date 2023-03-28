package com.example.messenger;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private List<User> users = new ArrayList<>();
    private  onUserClickListener onUserClickListener;

    public void setOnUserClickListener(UsersAdapter.onUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);
        String userInfo  = String.format("%s %s, %s",user.getName(), user.getLastName(), user.getAge());
        holder.textViewUserInfo.setText(userInfo);
int resBackGround;
        if(user.isOnline()){
            resBackGround =R.drawable.circle_green;

        }else {
            resBackGround = R.drawable.circle_red;
            }

        Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), resBackGround );
        holder.viewIsOnline.setBackground(drawable);

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (onUserClickListener != null){
            onUserClickListener.onUserClick(user);
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

interface  onUserClickListener {
        void onUserClick(User user);
}


    class UsersViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserInfo;
        private View viewIsOnline;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            viewIsOnline = itemView.findViewById(R.id.viewIsOnline);
        }


    }
}
