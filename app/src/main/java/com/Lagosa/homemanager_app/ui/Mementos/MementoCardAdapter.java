package com.Lagosa.homemanager_app.ui.Mementos;

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

public class MementoCardAdapter extends RecyclerView.Adapter<MementoCardAdapter.ViewHolder> {
    private final List<Map<String,Object>> mementosList;
    private final LayoutInflater inflater;

    public MementoCardAdapter(Context context, List<Map<String, Object>> mementosList) {
        this.mementosList = mementosList;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.memento_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mementosList.get(position).get("title").toString());
        holder.dueDate.setText(mementosList.get(position).get("dueDate").toString());
    }

    @Override
    public int getItemCount() {
        return mementosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, dueDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.mementoTitle);
            dueDate = itemView.findViewById(R.id.mementoDueDate);
        }
    }
}
