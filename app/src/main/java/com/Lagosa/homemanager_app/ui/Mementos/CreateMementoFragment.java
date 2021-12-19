package com.Lagosa.homemanager_app.ui.Mementos;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

public class CreateMementoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.create_memento,container,false);

        TextView title = myView.findViewById(R.id.txtAddMementoTitle);
        TextView dueDate = myView.findViewById(R.id.txtAddMementoDeadline);

        TextView dueDateWrapper = myView.findViewById(R.id.addMementoDeadlineWrapper);
        dueDateWrapper.setOnClickListener(new View.OnClickListener() {
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
                                dueDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                deadlinePicker.getDatePicker().setSpinnersShown(true);
                deadlinePicker.getDatePicker().setCalendarViewShown(false);
                deadlinePicker.show();
            }
        });

        Button createMemento = myView.findViewById(R.id.btnCreateMemento);
        createMemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().length() == 0 || title.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Give a title to the memento!",Toast.LENGTH_LONG).show();
                    return;
                }

                if(dueDate.getText().toString().length() == 0 || dueDate.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Pick a due date for the memento!",Toast.LENGTH_LONG).show();
                    return;
                }

                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                Cursor data = dbHelper.getUserId();
                data.moveToFirst();

                ServerCalls serverCalls = new ServerCalls(getContext());
                serverCalls.createMemento(UUID.fromString(data.getString(0)),title.getText().toString(), Date.valueOf(dueDate.getText().toString()));

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new MementoListFragment());
                fragmentTransaction.commit();
            }
        });

        return myView;
    }
}
