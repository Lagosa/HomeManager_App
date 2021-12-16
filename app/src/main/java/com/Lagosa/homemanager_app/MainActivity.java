package com.Lagosa.homemanager_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.ui.Authenticate.CreateFamily;
import com.Lagosa.homemanager_app.ui.Authenticate.JoinFamily;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btn_JoinFamily,btn_createFamily;
    ServerCalls serverCalls;
    TextView loadingScreenText;
    boolean serverStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loading_screen);
        loadingScreenText = findViewById(R.id.loadingScreenText);
        loadingScreenText.setText(R.string.connecting_server_loading_screen);

        serverCalls = new ServerCalls(this);
        serverCalls.wakeupCall();


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor data = dbHelper.getUserId();
        data.moveToFirst();
        if(data.getCount() > 0) {
            ServerCalls serverCalls = new ServerCalls(this);
            serverCalls.getUser(UUID.fromString(data.getString(0)));
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

    public void goToDrawer(Bundle user, Context context){
        Intent intent = new Intent(context,MainDrawerActivity.class);
        intent.putExtras(user);
        context.startActivity(intent);
        finish();
    }

}