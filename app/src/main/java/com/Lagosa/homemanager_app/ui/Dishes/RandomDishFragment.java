package com.Lagosa.homemanager_app.ui.Dishes;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.Database.DishCallback;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RandomDishFragment extends Fragment {

    private List<Map<String,Object>> dishTypes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.random_dish,container,false);

        TextView dishName = myView.findViewById(R.id.randomDishName);
        TextView dishType = myView.findViewById(R.id.randomDishType);
        TextView dishRecipe = myView.findViewById(R.id.randomDishRecipe);
        RecyclerView ingredientListContainer = myView.findViewById(R.id.randomDishDetailIngredientContainer);
        Spinner typeToGenerate = myView.findViewById(R.id.spnRandomDishType);

        ServerCalls serverCalls = new ServerCalls(getContext());
        serverCalls.getDishTypes(new DishCallback() {
            @Override
            public void gotAllDishes(List<Map<String, Object>> dishesList) {
                dishTypes = dishesList;
                List<String> dishTypes = new ArrayList<>();
                for(Map<String,Object> type: dishesList){
                    dishTypes.add(type.get("type").toString());
                }
                ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item,dishTypes);
                myAdapter.setDropDownViewResource(R.layout.spinner_item);
                typeToGenerate.setAdapter(myAdapter);
            }
        });

        typeToGenerate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                Cursor data = dbHelper.getUserId();
                data.moveToFirst();
                serverCalls.getRandomDish(new DishCallback() {
                    @Override
                    public void gotAllDishes(List<Map<String, Object>> dishesList) {
                        Map<String,Object> dish = dishesList.get(0);
                        dishName.setText(dish.get("name").toString());
                        dishType.setText(dish.get("type").toString());
                        dishRecipe.setText(dish.get("recipe").toString());

                        List<Map<String,Object>> ingredients = new ArrayList<>();
                        JSONArray ingredientsJson = (JSONArray) dish.get("ingredients");
                        for(int i = 0; i < ingredientsJson.length(); i++){
                            try {
                                JSONObject ingredientObject = ingredientsJson.getJSONObject(i);
                                Map<String,Object> ingredient = new HashMap<>();
                                ingredient.put("name",ingredientObject.getString("name"));
                                ingredient.put("quantity",ingredientObject.getString("quantity"));
                                ingredient.put("measurementUnit",ingredientObject.getString("measurementUnit"));
                                ingredients.add(ingredient);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        ingredientListContainer.setLayoutManager(new LinearLayoutManager(getContext()));
                        DishDetailIngredientsCardAdapter cardAdapter = new DishDetailIngredientsCardAdapter(getContext(),ingredients);
                        ingredientListContainer.setAdapter(cardAdapter);
                    }
                }, UUID.fromString(data.getString(0)), dishTypes.get(position).get("type").toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return myView;
    }
}
