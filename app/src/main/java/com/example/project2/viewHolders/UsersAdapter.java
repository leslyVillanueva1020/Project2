package com.example.project2.viewHolders;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.R;
import com.example.project2.database.entities.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    public interface OnUserAction {
        void onPromote(User user);
        void onDemote(User user);
        void onDelete(User user);
    }

    private List<User> users;
    private final OnUserAction listener;

    public UsersAdapter(List<User> users, OnUserAction listener) {
        this.users = users;
        this.listener = listener;
    }
    public void updateList(List<User> updated) {
        this.users = updated;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_card, parent, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvUsername.setText(user.getUsername());
        holder.tvRole.setText(user.isAdmin() ? "Role: Admin" : "Role: User");

        // Promote / demote logic
        if (user.isAdmin()) {
            holder.btnPromote.setText("Demote Admin");
            holder.btnPromote.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#6A1B9A"))
            );
            holder.btnPromote.setOnClickListener(v ->
                    listener.onDemote(user)
            );
        } else {
            holder.btnPromote.setText("Promote to Admin");
            holder.btnPromote.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#8f7383"))
            );
            holder.btnPromote.setOnClickListener(v ->
                    listener.onPromote(user)
            );
        }

        holder.btnDelete.setOnClickListener(v ->
                listener.onDelete(user)
        );
    }


    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvRole;
        Button btnPromote, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRole = itemView.findViewById(R.id.tvRole);
            btnPromote = itemView.findViewById(R.id.btnPromote);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
