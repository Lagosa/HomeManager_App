package com.Lagosa.homemanager_app.ui.Dishes;

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

public class PlanneDishDetailCardAdapter extends RecyclerView.Adapter<PlanneDishDetailCardAdapter.ViewHolder> {

    private final List<Map<String,String>> dishDetailList;
    private LayoutInflater inflater;

    public PlanneDishDetailCardAdapter(Context context, List<Map<String, String>> dishDetailList) {
        this.dishDetailList = dishDetailList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.planned_dish_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(dishDetailList.get(position).get("dishName"));
        holder.type.setText(dishDetailList.get(position).get("typeName"));
    }

    @Override
    public int getItemCount() {
        return dishDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plannedDishName);
            type = itemView.findViewById(R.id.plannedDishType);
        }
    }
}
