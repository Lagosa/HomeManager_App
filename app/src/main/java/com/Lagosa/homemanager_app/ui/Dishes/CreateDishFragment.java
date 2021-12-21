package com.Lagosa.homemanager_app.ui.Dishes;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.Database.DishCallback;
import com.Lagosa.homemanager_app.Database.IngredientCallback;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

;

public class CreateDishFragment extends Fragment {

    LinearLayout ingredientContainer;
    List<Map<String,String>> ingredientLists;
    List<Map<String,Object>> selectedIngredients;
    List<String> ingredientNames;
    ServerCalls serverCalls;
    int nrRowInContainer = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.create_dish,container,false);

        EditText name = myView.findViewById(R.id.pollCreateMessage);
        EditText recipe = myView.findViewById(R.id.txtAddDishRecipe);
        Spinner type = myView.findViewById(R.id.spnAddDishType);
        Spinner visibility = myView.findViewById(R.id.spnAddDishVisibility);
        Button addIngredient = myView.findViewById(R.id.createDishAddIngredientBTN);
        Button removeIngredient = myView.findViewById(R.id.createDishRemoveIngredientBTN);

        ingredientContainer = myView.findViewById(R.id.ingredientContainer);

        serverCalls = new ServerCalls(getContext());
        selectedIngredients = new ArrayList<>();
        serverCalls.getIngredients(new IngredientCallback() {
            @Override
            public void gotIngredients(List<Map<String, String>> ingredients) {
                Map<String,String> dummy = new HashMap<>();
                dummy.put("name","Select ingredient!");
                dummy.put("mu","-");
                ingredients.add(0,dummy);
                Map<String,String> dummy2 = new HashMap<>();
                dummy2.put("name","~Type a new ingredient~");
                dummy2.put("mu","-");
                ingredients.add(1,dummy2);
                ingredientLists = ingredients;
                ingredientNames = new ArrayList<>();
                int i = 0;
                for(Map<String,String> ingredient: ingredientLists){
                    ingredientNames.add(ingredient.get("name"));
                }
                addIngredientRow();

                addIngredient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addIngredientRow();
                    }
                });

                removeIngredient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeIngredientRow();
                    }
                });
            }
        });

        serverCalls.getDishTypes(new DishCallback() {
            @Override
            public void gotAllDishes(List<Map<String, Object>> dishesList) {
                List<String> dishTypes = new ArrayList<>();
                dishTypes.add("Dish type...");
                for(Map<String,Object> type: dishesList){
                    dishTypes.add(type.get("type").toString());
                }
                ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item,dishTypes);
                myAdapter.setDropDownViewResource(R.layout.spinner_item);
                type.setAdapter(myAdapter);
            }
        });

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item,getResources().getStringArray(R.array.dishVisibility));
        myAdapter.setDropDownViewResource(R.layout.spinner_item);
        visibility.setAdapter(myAdapter);

        Button create = myView.findViewById(R.id.createDishCreateBTN);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || name.getText().toString().length() == 0){
                    Toast.makeText(getContext(),"Give a name to the dish!",Toast.LENGTH_LONG).show();
                    return;
                }

                if(type.getSelectedItem().toString().equals("Dish type...")){
                    Toast.makeText(getContext(),"Select a dish type!",Toast.LENGTH_LONG).show();
                    return;
                }

                if(visibility.getSelectedItem().toString().equals("Visibility...")){
                    Toast.makeText(getContext(),"Select visibility!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(recipe.getText().toString().isEmpty() || recipe.getText().length() == 0){
                    Toast.makeText(getContext(),"Please provide a recipe!",Toast.LENGTH_LONG).show();
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                Cursor data = dbHelper.getUserId();
                data.moveToFirst();

                serverCalls.insertDish(UUID.fromString(data.getString(0)),name.getText().toString(),type.getSelectedItem().toString(),visibility.getSelectedItem().toString(),recipe.getText().toString(),selectedIngredients);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new DishListFragment());
                fragmentTransaction.commit();
            }
        });

        return myView;
    }

    private void addIngredientRow(){
        View ingredientRow = getLayoutInflater().inflate(R.layout.create_dish_ingredient_row_spn,null,false);
        nrRowInContainer++;
        ingredientRow.setTag(nrRowInContainer);

        Spinner ingredientName = ingredientRow.findViewById(R.id.spnAddDishIngredient);
        EditText quantity = ingredientRow.findViewById(R.id.txtAddIngredientQuantity);
        TextView measurementUnit = ingredientRow.findViewById(R.id.txtAddIngredientMeasurementUnit);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, ingredientNames);
        myAdapter.setDropDownViewResource(R.layout.spinner_item);
        ingredientName.setAdapter(myAdapter);

        ingredientName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w("CreateDish","ing: " + ingredientLists.get(position).get("name"));
                if(ingredientLists.get(position).get("name").equals("~Type a new ingredient~")){
                    replaceSpinner((Integer) ingredientRow.getTag());
                }
                measurementUnit.setText(ingredientLists.get(position).get("mu"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(),"Please select an ingredient!",Toast.LENGTH_LONG).show();
            }
        });

        quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Map<String,Object> ingredient =  new HashMap<>();
                    ingredient.put("name",ingredientName.getSelectedItem().toString());
                    ingredient.put("quantity",quantity.getText().toString());
                    ingredient.put("measurementUnit",measurementUnit.getText().toString());
                    if( !ingredientName.getSelectedItem().toString().equals("~Type a new ingredient~") && !selectedIngredients.contains(ingredient)){
                        selectedIngredients.add(ingredient);
                    }
                }
            }
        });



        ingredientContainer.addView(ingredientRow);
    }

    private void removeIngredientRow(){
        ingredientContainer.removeViewAt(ingredientContainer.getChildCount()-1);
        nrRowInContainer--;
    }

    private void replaceSpinner(int position){
        View ingredientRow = getLayoutInflater().inflate(R.layout.create_dish_ingredient_row_txt,null,false);

        EditText name = ingredientRow.findViewById(R.id.txtAddIngredientName);
        EditText quantity = ingredientRow.findViewById(R.id.txtAddIngredientQuantity);
        EditText measurementUnit = ingredientRow.findViewById(R.id.txtAddIngredientMeasurementUnit);

        Map<String,Object> ingredientCreated = new HashMap<>();
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ingredientCreated.put("name",name.getText().toString());

                if(ingredientCreated.get("quantity") != null && ingredientCreated.get("measurementUnit") != null && !selectedIngredients.contains(ingredientCreated)){
                    selectedIngredients.add(ingredientCreated);
                }
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ingredientCreated.put("quantity",quantity.getText().toString());
                if(ingredientCreated.get("name") != null && ingredientCreated.get("measurementUnit") != null && !selectedIngredients.contains(ingredientCreated)){
                    selectedIngredients.add(ingredientCreated);
                }
            }
        });

        measurementUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ingredientCreated.put("measurementUnit",measurementUnit.getText().toString());
                if(ingredientCreated.get("quantity") != null && ingredientCreated.get("name") != null && !selectedIngredients.contains(ingredientCreated)){
                    selectedIngredients.add(ingredientCreated);
                }
            }
        });
        Log.w(  "CreateDish","Replacing view" + position);
        ingredientContainer.removeViewAt(position-1);
        ingredientContainer.addView(ingredientRow,position-1);
    }
}
