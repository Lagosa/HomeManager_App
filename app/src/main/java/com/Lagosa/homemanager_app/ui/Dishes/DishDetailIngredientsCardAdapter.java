package com.Lagosa.homemanager_app.ui.Dishes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.R;

import java.util.List;
import java.util.Map;

public class DishDetailIngredientsCardAdapter extends RecyclerView.Adapter<DishDetailIngredientsCardAdapter.ViewHolder> {

    private final List<Map<String,Object>> ingredientList;
    private final LayoutInflater inflater;

    public DishDetailIngredientsCardAdapter(Context context, List<Map<String, Object>> ingredientList) {
        this.ingredientList = ingredientList;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.create_dish_ingredient_row_txt,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ingredient.setText(ingredientList.get(position).get("name").toString());
        holder.quantity.setText(ingredientList.get(position).get("quantity").toString());
        holder.measurementUnit.setText(ingredientList.get(position).get("measurementUnit").toString());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText ingredient,quantity,measurementUnit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.txtAddIngredientName);
            quantity = itemView.findViewById(R.id.txtAddIngredientQuantity);
            measurementUnit = itemView.findViewById(R.id.txtAddIngredientMeasurementUnit);
        }
    }
}
