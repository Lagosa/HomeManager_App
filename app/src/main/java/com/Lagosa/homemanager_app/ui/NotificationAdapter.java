package com.Lagosa.homemanager_app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.R;

import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<Map<String,String>> notificationList;
    LayoutInflater inflater;

    public NotificationAdapter(Context context, List<Map<String, String>> notificationList) {
        this.notificationList = notificationList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notification_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(notificationList.get(position).get("title"));
        holder.date.setText(notificationList.get(position).get("date"));
        holder.sender.setText(notificationList.get(position).get("sender"));
        holder.receiver.setText(notificationList.get(position).get("receiver"));
        holder.message.setText(notificationList.get(position).get("message"));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,date,sender,receiver,message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notificationCardTitle);
            date = itemView.findViewById(R.id.notificationCardDate);
            sender = itemView.findViewById(R.id.notificationCardSender);
            receiver = itemView.findViewById(R.id.notificationCardReceiver);
            message = itemView.findViewById(R.id.notificationCardMessage);
        }
    }
}
