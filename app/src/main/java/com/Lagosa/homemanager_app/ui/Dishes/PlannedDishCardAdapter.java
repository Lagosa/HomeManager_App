package com.Lagosa.homemanager_app.ui.Dishes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlannedDishCardAdapter extends RecyclerView.Adapter<PlannedDishCardAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Map<String,Object>> plannedDishList;

    public PlannedDishCardAdapter(Context context, List<Map<String,Object>> plannedDishList){
        this.plannedDishList = plannedDishList;
        inflater = LayoutInflater.from(context);
        Log.w("DishPlan","Creating a new Adapter");
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.planned_dishes_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.w("DishPlan","adding data to card");
        holder.date.setText(plannedDishList.get(position).get("date").toString());

        holder.container.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        PlanneDishDetailCardAdapter detailCardAdapter = new PlanneDishDetailCardAdapter(inflater.getContext(),(ArrayList) plannedDishList.get(position).get("dishList"));
        holder.container.setAdapter(detailCardAdapter);
    }

    @Override
    public int getItemCount() {
        return plannedDishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        RecyclerView container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.plannedDishCardDate);
            container = itemView.findViewById(R.id.plannedDishCardContainer);
        }
    }
}
