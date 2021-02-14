package com.smproductions.flingg.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smproductions.flingg.Model.Chat;
import com.smproductions.flingg.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private final Context mContext;
    private final List<Chat> mChats;

    FirebaseUser fUser;

    public ChatAdapter(Context mContext, List<Chat> mChats) {
        this.mChats = mChats;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_right_chat_bubble, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_left_chat_bubble, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Chat chat = mChats.get(position);

        holder.chatBubble.setText(chat.getMessage());

        if (position == mChats.size() - 1) {
            if (chat.isSeen()) {
                holder.doubleTicks.setImageResource(R.drawable.ic_double_ticks);
            } else {
                holder.doubleTicks.setImageResource(R.drawable.ic_single_tick);
            }
        } else {
            holder.doubleTicks.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView chatBubble;
        public ImageView doubleTicks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatBubble = itemView.findViewById(R.id.ChatBbl_text);
            doubleTicks = itemView.findViewById(R.id.ChatBbl_doubleTicks);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(fUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
