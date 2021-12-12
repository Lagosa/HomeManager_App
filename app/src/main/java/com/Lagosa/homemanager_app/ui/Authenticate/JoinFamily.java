package com.Lagosa.homemanager_app.ui.Authenticate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.MainActivity;
import com.Lagosa.homemanager_app.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.UUID;

public class JoinFamily extends AppCompatActivity {

    EditText txt_joinCode,txt_nickname,txt_birthday;
    TextView txt_birthdayWrapper;
    Spinner spn_role;
    DatePickerDialog dt_datePicker;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_family);

        txt_joinCode = findViewById(R.id.txtJoinFamilyJoinCode);
        txt_nickname = findViewById(R.id.txtJoinNickname);
        txt_birthday = findViewById(R.id.txtBirthdate);
        txt_birthdayWrapper = findViewById(R.id.birthdateWrapper);

        spn_role = findViewById(R.id.spnJoinRole);
        setUpSpinner(spn_role);

        dbHelper = new DatabaseHelper(this);

        Bundle bundle = getIntent().getExtras();
        Log.w("JoinFAMILY","Bundle got!");
        if(bundle != null){
             int joinCode = bundle.getInt("joinCode");
             Log.w("JoinFamily","Join Code: " + joinCode);
            txt_joinCode.setText(joinCode + "");
        }

        txt_birthdayWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                dt_datePicker = new DatePickerDialog(JoinFamily.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear += 1;
                                txt_birthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                dt_datePicker.getDatePicker().setSpinnersShown(true);
                dt_datePicker.getDatePicker().setCalendarViewShown(false);
                dt_datePicker.show();
            }
        });

        Button btn_joinFamily = findViewById(R.id.btnJoinFamily);
        btn_joinFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int joinCode = Integer.parseInt(txt_joinCode.getText().toString());
                if(joinCode < 100000){
                    Toast.makeText(JoinFamily.this,"Invalid join code!",Toast.LENGTH_LONG).show();
                    return;
                }

                String nickname = txt_nickname.getText().toString();
                if(nickname.length() == 0){
                    Toast.makeText(JoinFamily.this,"Please provide a nickname!",Toast.LENGTH_LONG).show();
                    return;
                }

                String birthdate = txt_birthday.getText().toString();
                if(birthdate.length() == 0){
                    Toast.makeText(JoinFamily.this,"Please provide a valid birthdate",Toast.LENGTH_LONG).show();
                    return;
                }

                String role = spn_role.getSelectedItem().toString();
                if(role.equals("Role...")){
                    Toast.makeText(JoinFamily.this,"Select a role!",Toast.LENGTH_LONG).show();
                    return;
                }

                String url = getString(R.string.server_url) + "authentication/joinFamily/" + joinCode + "/" + nickname + "/" + role + "/" + birthdate;

                RequestQueue queue = Volley.newRequestQueue(JoinFamily.this);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UUID userId = UUID.fromString(response);
                        dbHelper.addUserId(userId);
                        Toast.makeText(JoinFamily.this,"User added!",Toast.LENGTH_SHORT).show();
                        goToNewPage(MainActivity.class);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                queue.add(stringRequest);
            }
        });

    }

    private void setUpSpinner(Spinner spn_role){
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(JoinFamily.this,
                R.layout.spinner_item,getResources().getStringArray(R.array.familyRoles));
        myAdapter.setDropDownViewResource(R.layout.spinner_item);
        spn_role.setAdapter(myAdapter);
    }

    private void goToNewPage(Class myClass) {
        Intent intent = new Intent(this, myClass);
        startActivity(intent);
        finish();
    }
}
