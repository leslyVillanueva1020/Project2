package com.example.project2.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.R;
import com.example.project2.database.entities.User;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public TextView tvUsername, tvRole;
    public Button btnPromote, btnDelete;

    public UserViewHolder(View itemView) {
        super(itemView);
        tvUsername = itemView.findViewById(R.id.tvUsername);
        tvRole = itemView.findViewById(R.id.tvRole);
        btnPromote = itemView.findViewById(R.id.btnPromote);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }

    public void bind(User user, View.OnClickListener promoteListener, View.OnClickListener deleteListener) {
        tvUsername.setText(user.getUsername());
        tvRole.setText(user.isAdmin() ? "Role: Admin" : "Role: User");
        btnPromote.setVisibility(user.isAdmin() ? View.GONE : View.VISIBLE);
        btnPromote.setOnClickListener(promoteListener);
        btnDelete.setOnClickListener(deleteListener);
    }
}
