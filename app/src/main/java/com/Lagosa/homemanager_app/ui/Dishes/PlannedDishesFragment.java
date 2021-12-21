package com.Lagosa.homemanager_app.ui.Dishes;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.ViewModels.DishPlansViewModel;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PlannedDishesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.view_planned_dishes_fragment,container,false);

        Button createPlan = myView.findViewById(R.id.planDishCreate);
        createPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new CreatePlanFragment());
                fragmentTransaction.commit();
            }
        });

        TextView startDateWrapper = myView.findViewById(R.id.txtPlannedDishesStartWrapper);
        EditText startDate = myView.findViewById(R.id.txtPlannedDishesStart);

        DishPlansViewModel viewModel = new ViewModelProvider(getActivity()).get(DishPlansViewModel.class);
        viewModel.setPlanContainer(myView.findViewById(R.id.plannedDishesRecyclerView));

        startDateWrapper.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog startDatePicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear += 1;
                                startDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                startDatePicker.getDatePicker().setSpinnersShown(true);
                startDatePicker.getDatePicker().setCalendarViewShown(false);
                startDatePicker.show();
            }
        });

        TextView endDateWrapper = myView.findViewById(R.id.txtPlannedDishesEndWrapper);
        EditText endDate = myView.findViewById(R.id.txtPlannedDishesEnd);


        endDateWrapper.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog endDatePicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear += 1;
                                endDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);

                                Map<String, Date> dateInterval = new HashMap<>();
                                dateInterval.put("start",Date.valueOf(startDate.getText().toString()));
                                dateInterval.put("end",Date.valueOf(endDate.getText().toString()));

                                Log.w("DishPlan","End interval got, sending back");
                                viewModel.setDateInterval(dateInterval);
                            }
                        }, year, month, day);
                endDatePicker.getDatePicker().setSpinnersShown(true);
                endDatePicker.getDatePicker().setCalendarViewShown(false);
                endDatePicker.show();
            }
        });



        return myView;
    }
}
