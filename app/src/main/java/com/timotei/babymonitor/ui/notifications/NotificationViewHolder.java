package com.timotei.babymonitor.ui.notifications;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.timotei.babymonitor.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder{
    protected TextView title;
    protected TextView description;
    protected TextView date;
    protected ImageView icon;

    public NotificationViewHolder(@NonNull View itemView){
        super(itemView);

        title=itemView.findViewById(R.id.notificationTitle);
        description= itemView.findViewById(R.id.description);
        date=itemView.findViewById(R.id.notificationDate);
        icon=itemView.findViewById(R.id.icon);
    }
}
