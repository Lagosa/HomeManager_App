package com.Lagosa.homemanager_app.ui.Dishes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetIngredientsCardAdapter extends RecyclerView.Adapter<GetIngredientsCardAdapter.ViewHolder> {

    List<Map<String,Object>> dishList;
    LayoutInflater inflater;

    public GetIngredientsCardAdapter(Context context, List<Map<String,Object>> dishList){
        this.dishList = dishList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.get_ingredients_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dishName.setText(dishList.get(position).get("dishName").toString());

        List<Map<String,String>> ingredientList = new ArrayList<>();
        JSONArray ingredientArray = (JSONArray) dishList.get(position).get("ingredients");
        for(int i = 0 ;i < ingredientArray.length(); i++){
            try {
                JSONObject ingredient = ingredientArray.getJSONObject(i);
                Map<String,String> ingredientMap = new HashMap<>();
                ingredientMap.put("name",ingredient.getString("name"));
                ingredientMap.put("measurementUnit",ingredient.getString("measurementUnit"));
                ingredientMap.put("quantity",ingredient.getString("quantity"));
                ingredientList.add(ingredientMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        holder.ingredientContainer.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        GetIngredientsIngredientsCardAdapter adapter = new GetIngredientsIngredientsCardAdapter(inflater.getContext(), ingredientList);
        holder.ingredientContainer.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishName;
        RecyclerView ingredientContainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.getIngredientsDishName);
            ingredientContainer = itemView.findViewById(R.id.getIngredientsList);
        }
    }
}
