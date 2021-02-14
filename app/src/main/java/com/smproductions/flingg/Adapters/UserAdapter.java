package com.smproductions.flingg.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smproductions.flingg.ChatActivity;
import com.smproductions.flingg.Model.Chat;
import com.smproductions.flingg.Model.User;
import com.smproductions.flingg.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context mContext;
    private final List<User> mUsers;
    //private boolean isOnorOff;

    public UserAdapter(Context mContext, List<User> mUsers) {         // insert boolean isOnorOff at the end
        this.mUsers = mUsers;
        this.mContext = mContext;
        //this.isOnorOff = isOnorOff;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);

        holder.name.setText(user.getName());
        holder.username.setText(user.getUsername());

        if (user.getImageURL().equals("default")) {
            holder.profileImage.setImageResource(R.drawable.user);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profileImage);
        }

        /**if (isOnorOff) {
            if (user.getStatus().equals("online")) {
                holder.statusOn.setVisibility(View.VISIBLE);
                holder.statusOff.setVisibility(View.GONE);
            } else {
                holder.statusOff.setVisibility(View.VISIBLE);
                holder.statusOn.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show();
            holder.statusOn.setVisibility(View.GONE);
            holder.statusOff.setVisibility(View.GONE);
        }*/

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra("userID", user.getId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, username;
        public ImageView profileImage;
        private ImageView statusOn, statusOff;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.itUser_name);
            username = itemView.findViewById(R.id.itUser_username);
            profileImage = itemView.findViewById(R.id.itUser_profileImage);
            statusOn = itemView.findViewById(R.id.itUser_statusDotOn);
            statusOff = itemView.findViewById(R.id.itUser_statusDotOff);
        }
    }
}
