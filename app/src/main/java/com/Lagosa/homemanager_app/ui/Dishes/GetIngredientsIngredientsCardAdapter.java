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

public class GetIngredientsIngredientsCardAdapter extends RecyclerView.Adapter<GetIngredientsIngredientsCardAdapter.ViewHolder> {

    List<Map<String,String>> ingredientList;
    LayoutInflater inflater;

    public GetIngredientsIngredientsCardAdapter(Context context, List<Map<String, String>> ingredientList) {
        this.ingredientList = ingredientList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.get_ingredients_ingredients_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ingredient.setText(ingredientList.get(position).get("name"));
        holder.quantity.setText(ingredientList.get(position).get("quantity"));
        holder.mu.setText(ingredientList.get(position).get("measurementUnit"));
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredient, quantity, mu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredient = itemView.findViewById(R.id.getIngredientsingredientsIngredientName);
            quantity = itemView.findViewById(R.id.getIngredientsIngredientsQuantity);
            mu = itemView.findViewById(R.id.getIngredientsIngredientsMU);
        }
    }
}
