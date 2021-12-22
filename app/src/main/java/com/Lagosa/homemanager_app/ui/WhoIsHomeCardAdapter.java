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

public class WhoIsHomeCardAdapter extends RecyclerView.Adapter<WhoIsHomeCardAdapter.ViewHolder> {

    List<String> nickname;
    LayoutInflater inflater;

    public WhoIsHomeCardAdapter(Context context, List<String> nickname) {
        this.nickname = nickname;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.who_is_home_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nickname.setText(nickname.get(position));
    }

    @Override
    public int getItemCount() {
        return nickname.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.WhoIsHomeNickname);
        }
    }
}
