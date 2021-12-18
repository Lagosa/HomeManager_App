package com.Lagosa.homemanager_app.ui.Chores;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Lagosa.homemanager_app.Database.ChoreTypesListCallback;
import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CreateChoreFragment extends Fragment implements View.OnClickListener {

    EditText title, description,deadline;
    TextView  deadlineWrapper;
    Spinner choreTypeSpinner;
    DatePickerDialog deadlinePicker;

    ServerCalls serverCalls;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup myView = (ViewGroup) inflater.inflate(R.layout.create_chore,container,false);

        serverCalls = new ServerCalls(getContext());

        title = myView.findViewById(R.id.txtAddChoreTitle);
        description = myView.findViewById(R.id.txtAddChoreDescription);

        choreTypeSpinner = myView.findViewById(R.id.spnAddChoreType);
        setUpSpinner(choreTypeSpinner);

        deadlineWrapper = myView.findViewById(R.id.addChoreDeadlineWrapper);
        deadline = myView.findViewById(R.id.txtAddChoreDeadline);


        deadlineWrapper.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                deadlinePicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear += 1;
                                deadline.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                deadlinePicker.getDatePicker().setSpinnersShown(true);
                deadlinePicker.getDatePicker().setCalendarViewShown(false);
                deadlinePicker.show();
            }
        });

        Button btnCreateChore = myView.findViewById(R.id.btnAddChoreCreate);
        btnCreateChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                Cursor data = dbHelper.getUserId();
                data.moveToFirst();
                UUID userId = UUID.fromString(data.getString(0));

                serverCalls.createChore(userId, Date.valueOf(deadline.getText().toString()), choreTypeSpinner.getSelectedItem().toString(), title.getText().toString(), description.getText().toString());


                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new AllChoresListFragment());
                fragmentTransaction.commit();
            }
        });

        return myView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setUpSpinner(Spinner spn_role){
        Log.w("CreateChore","Set up spinner called!");
        serverCalls.getChoreTypes(new ChoreTypesListCallback() {
            @Override
            public void setChoreTypesList(List<String> choreTypesResp) {
                    Log.w("CreateChore","Chore types got!");
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, choreTypesResp);
                    myAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spn_role.setAdapter(myAdapter);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
