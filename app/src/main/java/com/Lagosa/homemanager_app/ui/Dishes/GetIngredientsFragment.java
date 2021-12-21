package com.Lagosa.homemanager_app.ui.Dishes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.ViewModels.DishesViewModel;

import java.util.Calendar;

public class GetIngredientsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_get_ingredients_fragment,container,false);

        TextView dayToGet = view.findViewById(R.id.txtGetIngredientDate);

        DishesViewModel viewModel = new ViewModelProvider(requireActivity()).get(DishesViewModel.class);

        viewModel.setDishListRecyclerView(view.findViewById(R.id.getIngredientsDishList));

        TextView dayWrapper = view.findViewById(R.id.txtGetIngredientDateWrapper);
        dayWrapper.setOnClickListener(new View.OnClickListener() {
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
                                dayToGet.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                                viewModel.setDataFromFragment(dayToGet.getText().toString());
                            }
                        }, year, month, day);
                deadlinePicker.getDatePicker().setSpinnersShown(true);
                deadlinePicker.getDatePicker().setCalendarViewShown(false);
                deadlinePicker.show();
            }
        });

        return view;
    }
}
