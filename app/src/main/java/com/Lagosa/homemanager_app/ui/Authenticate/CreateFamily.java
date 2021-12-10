package com.Lagosa.homemanager_app.ui.Authenticate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.logging.Logger;

public class CreateFamily extends AppCompatActivity {

    private EditText txt_email,txt_password,txt_passwordAgain;
    private UUID familyId;
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_family);

        txt_email = findViewById(R.id.txtCreateEmail);
        txt_password = findViewById(R.id.txtCreatePassword);
        txt_passwordAgain = findViewById(R.id.txtCreatePasswordRepeat);

        Button createButton = findViewById(R.id.btn_createFamily);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = txt_password.getText().toString();
                String passwordRe = txt_passwordAgain.getText().toString();
                if(!password.equals(passwordRe)){
                    Toast toast = Toast.makeText(getApplicationContext(),"Passwords do not match!",Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if(txt_password.length() < 8){
                    Toast toast = Toast.makeText(getApplicationContext(),"The password provided is too short, at least 8 characters!",Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                String email = txt_email.getText().toString().trim();

                if(email.length() == 0){
                    Toast.makeText(CreateFamily.this,"Email field is empty!",Toast.LENGTH_LONG).show();
                    return;
                }

                ServerCalls serverCalls = new ServerCalls(CreateFamily.this);
                serverCalls.registerFamily(email,password);
            }
        });
    }
}
