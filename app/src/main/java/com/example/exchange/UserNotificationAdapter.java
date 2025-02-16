package com.example.exchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserNotificationAdapter extends RecyclerView.Adapter<UserNotificationAdapter.NotificationViewHolder> {
    private List<UserNotificationClass> notificationList;

    public UserNotificationAdapter(List<UserNotificationClass> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        UserNotificationClass notification = notificationList.get(position);
        holder.messageTextView.setText(notification.getMessage());
        holder.dateTextView.setText(notification.getNotifDate());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView, dateTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.notificationMessage);
            dateTextView = itemView.findViewById(R.id.notificationDate);
        }
    }
}
