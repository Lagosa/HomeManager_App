package com.Lagosa.homemanager_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.Database.JoinCodeCallback;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.ui.JoincodeFragment;
import com.Lagosa.homemanager_app.ui.ViewModels.JoinCodeViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.Map;
import java.util.UUID;

public class MainDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    View header;
    ServerCalls serverCalls;
    DatabaseHelper dbHelper;
    Map<String,Object> user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        header = navigationView.getHeaderView(0);
        TextView txt_nickname = header.findViewById(R.id.txtDrawerNickname);
        TextView txt_role = header.findViewById(R.id.txtDrawerRole);

        Intent intent = getIntent();
        txt_nickname.setText(intent.getStringExtra("nickName"));
        txt_role.setText(intent.getStringExtra("role"));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.getFamilyId:
                displayJoinCodeFragment();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    void displayJoinCodeFragment(){
        JoincodeFragment joinCodeFragment = new JoincodeFragment();
        fragmentManager = getSupportFragmentManager();


        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment,joinCodeFragment);
        fragmentTransaction.commit();

        Intent intent = getIntent();
        String familyId = intent.getStringExtra("familyId");
        serverCalls = new ServerCalls(this);
        serverCalls.getJoinCode(new JoinCodeCallback() {
            @Override
            public void onSuccess(int joinCode) {
                JoinCodeViewModel viewModel = new ViewModelProvider(MainDrawerActivity.this).get(JoinCodeViewModel.class);
                viewModel.setJoinCode(joinCode);
            }
        }, UUID.fromString(familyId));
    }
}
