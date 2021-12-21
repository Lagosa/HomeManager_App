package com.Lagosa.homemanager_app.ui.Dishes;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.Database.DishCallback;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreatePlanFragment extends Fragment {

    private LinearLayout planContainer;
    private List<Map<String,Object>> dishList;
    private List<String> dishnamesList = new ArrayList<>();
    private List<Map<String,String>> selectedDishesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.create_plan,container,false);

        Button addRow = myView.findViewById(R.id.planDishAddRow);
        Button removeRow = myView.findViewById(R.id.planDishRemoveRow);
        planContainer = myView.findViewById(R.id.createPlanContainer);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor data = dbHelper.getUserId();
        data.moveToFirst();
        UUID userId = UUID.fromString(data.getString(0));

        ServerCalls serverCalls = new ServerCalls(getContext());
        serverCalls.getAllDishes(new DishCallback() {
            @Override
            public void gotAllDishes(List<Map<String, Object>> dishesList) {
                dishList = dishesList;

                for(Map<String,Object> dish : dishList){
                    dishnamesList.add(dish.get("name").toString());
                }
                addRow();
            }
        }, userId);

        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow();
            }
        });

        removeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRow();
            }
        });

        Button submit = myView.findViewById(R.id.planDishCreateSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 1;
                for(Map<String,String> plan : selectedDishesList){
                    serverCalls.createPlan(userId,plan.get("dishId"), Date.valueOf(plan.get("day")),i,selectedDishesList.size());
                    i++;
                }
            }
        });

        return myView;
    }

    private void addRow(){
        View row = getLayoutInflater().inflate(R.layout.create_plan_row,null,false);

        Spinner dishName = row.findViewById(R.id.spnAddPlanDishName);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, dishnamesList);
        myAdapter.setDropDownViewResource(R.layout.spinner_item);
        dishName.setAdapter(myAdapter);

        TextView selectedDishId = row.findViewById(R.id.planDishCreateDishId);

        dishName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDishId.setText(dishList.get(dishnamesList.indexOf(dishName.getSelectedItem().toString())).get("id").toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView dayToPrepareWrapper = row.findViewById(R.id.txtPlannedDishesCreateDayWrapper);
        EditText dayToPrepare = row.findViewById(R.id.txtPlannedDishesCreateDay);
        dayToPrepareWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog deadlinePicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear += 1;
                                dayToPrepare.setText(year + "-" + monthOfYear + "-" + dayOfMonth);

                                Map<String,String> plannedDishInfo = new HashMap<>();
                                plannedDishInfo.put("dishId",selectedDishId.getText().toString());
                                plannedDishInfo.put("day",dayToPrepare.getText().toString());

                                selectedDishesList.add(plannedDishInfo);
                            }
                        }, year, month, day);
                deadlinePicker.getDatePicker().setSpinnersShown(true);
                deadlinePicker.getDatePicker().setCalendarViewShown(false);
                deadlinePicker.show();
            }
        });

        planContainer.addView(row);
    }

    private void removeRow(){
        planContainer.removeViewAt(planContainer.getChildCount()-1);
    }
}
