package com.Lagosa.homemanager_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.ui.Authenticate.CreateFamily;
import com.Lagosa.homemanager_app.ui.Authenticate.JoinFamily;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btn_JoinFamily,btn_createFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor data = dbHelper.getUserId();
        if(data.getCount() > 0) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        setContentView(R.layout.welcome_page);

        btn_JoinFamily = findViewById(R.id.joinFamily);
        btn_createFamily = findViewById(R.id.createFamily);

        btn_JoinFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNewPage(JoinFamily.class);
            }
        });

        btn_createFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNewPage(CreateFamily.class);
            }
        });
    }
    public void goToNewPage(Class myClass) {
        Intent intent = new Intent(this, myClass);
        startActivity(intent);
        finish();
    }

}