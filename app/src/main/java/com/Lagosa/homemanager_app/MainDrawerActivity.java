package com.Lagosa.homemanager_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.Lagosa.homemanager_app.Database.ChoreMyChoresCallback;
import com.Lagosa.homemanager_app.Database.ChoreNotDoneListCallback;
import com.Lagosa.homemanager_app.Database.DishCallback;
import com.Lagosa.homemanager_app.Database.JoinCodeCallback;
import com.Lagosa.homemanager_app.Database.MementoCallback;
import com.Lagosa.homemanager_app.Database.ReportCallback;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.ui.Chores.AllChoresListFragment;
import com.Lagosa.homemanager_app.ui.Chores.Chore;
import com.Lagosa.homemanager_app.ui.Chores.ChoreCardAdapter;
import com.Lagosa.homemanager_app.ui.Chores.MyChoreCardAdapter;
import com.Lagosa.homemanager_app.ui.Chores.MyChoresListFragment;
import com.Lagosa.homemanager_app.ui.Dishes.DishListFragment;
import com.Lagosa.homemanager_app.ui.Dishes.DishPreviewCardAdapter;
import com.Lagosa.homemanager_app.ui.JoincodeFragment;
import com.Lagosa.homemanager_app.ui.Mementos.MementoCardAdapter;
import com.Lagosa.homemanager_app.ui.Mementos.MementoListFragment;
import com.Lagosa.homemanager_app.ui.Reports.Report;
import com.Lagosa.homemanager_app.ui.Reports.ReportCardAdapter;
import com.Lagosa.homemanager_app.ui.Reports.ReportListFragment;
import com.Lagosa.homemanager_app.ui.ViewModels.ChoreViewModel;
import com.Lagosa.homemanager_app.ui.ViewModels.DishesViewModel;
import com.Lagosa.homemanager_app.ui.ViewModels.JoinCodeViewModel;
import com.Lagosa.homemanager_app.ui.ViewModels.MementoViewModel;
import com.Lagosa.homemanager_app.ui.ViewModels.ReportViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
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
    Intent intent;
    UUID familyId;
    UUID userId;

    MenuItem searchFilter;

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

        intent = getIntent();
        familyId = UUID.fromString(intent.getStringExtra("familyId"));
        userId = UUID.fromString(intent.getStringExtra("id"));

        txt_nickname.setText(intent.getStringExtra("nickName"));
        txt_role.setText(intent.getStringExtra("role"));

        serverCalls = new ServerCalls(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.getFamilyJoinCode:
                displayJoinCodeFragment();
                break;
            case R.id.listChores:
                listChores();
                break;
            case R.id.myChores:
                listMyChores();
                break;
            case R.id.getReport:
                getReport();
                break;
            case R.id.getMementos:
                getMementos();
                break;
            case R.id.listAllDishes:
                listAllDishes();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayJoinCodeFragment(){
        fragmentManager = getSupportFragmentManager();


        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment,new JoincodeFragment());
        fragmentTransaction.commit();

        serverCalls.getJoinCode(new JoinCodeCallback() {
            @Override
            public void onSuccess(int joinCode) {
                JoinCodeViewModel viewModel = new ViewModelProvider(MainDrawerActivity.this).get(JoinCodeViewModel.class);
                viewModel.setJoinCode(joinCode);
            }
        }, familyId);
    }

    private void listChores(){
        Log.w("CHORES","Generating list!");
        serverCalls.getAllNotDoneChores(new ChoreNotDoneListCallback() {
            @Override
            public void setNotDoneChoreList(List<Chore> chores) {
                Log.w("CHORES","Method called!");
                ChoreViewModel viewModel = new ViewModelProvider(MainDrawerActivity.this).get(ChoreViewModel.class);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new AllChoresListFragment());
                fragmentTransaction.commit();

                viewModel.getNotDoneListRecycleViewFamily().observe(MainDrawerActivity.this,item ->{

                    Log.w("CHORES","Got recycle view");

                    item.setLayoutManager(new LinearLayoutManager(MainDrawerActivity.this));
                    ChoreCardAdapter adapter = new ChoreCardAdapter(MainDrawerActivity.this,chores,userId);
                    item.setAdapter(adapter);
                });
            }
        },userId);
    }

    private void listMyChores(){
        serverCalls.listMyChores(new ChoreMyChoresCallback() {
            @Override
            public void gotMyChores(List<Chore> myChores) {

                ChoreViewModel viewModel = new ViewModelProvider(MainDrawerActivity.this).get(ChoreViewModel.class);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new MyChoresListFragment());
                fragmentTransaction.commit();

                viewModel.getMyChoresList().observe(MainDrawerActivity.this,item ->{
                    item.setLayoutManager(new LinearLayoutManager(MainDrawerActivity.this));
                    MyChoreCardAdapter adapter = new MyChoreCardAdapter(MainDrawerActivity.this, myChores, userId);
                    item.setAdapter(adapter);
                });
            }
        },userId);
    }

    private void getReport(){
        serverCalls.getReport(new ReportCallback() {
            @Override
            public void gotReport(List<Report> reportList) {
                ReportViewModel viewModel = new ViewModelProvider(MainDrawerActivity.this).get(ReportViewModel.class);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new ReportListFragment());
                fragmentTransaction.commit();

                viewModel.getReportRecyclerView().observe(MainDrawerActivity.this,item ->{
                    item.setLayoutManager(new LinearLayoutManager(MainDrawerActivity.this));
                    ReportCardAdapter adapter = new ReportCardAdapter(MainDrawerActivity.this,MainDrawerActivity.this,reportList);
                    item.setAdapter(adapter);
                });
            }
        },userId);
    }

    private void getMementos(){
        serverCalls.getMementos(new MementoCallback(){

            @Override
            public void gotMementos(List<Map<String, Object>> mementoList) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new MementoListFragment());
                fragmentTransaction.commit();

                MementoViewModel viewModel = new ViewModelProvider(MainDrawerActivity.this).get(MementoViewModel.class);
                viewModel.getRecyclerViewMutableLiveData().observe(MainDrawerActivity.this,item->{
                    item.setLayoutManager(new LinearLayoutManager(MainDrawerActivity.this));
                    MementoCardAdapter adapter = new MementoCardAdapter(MainDrawerActivity.this,mementoList);
                    item.setAdapter(adapter);
                });
            }
        },userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_dish,menu);

        MenuItem searchFilter = menu.findItem(R.id.search_dish);
        searchFilter.setVisible(false);

        this.searchFilter= searchFilter;

        return super.onCreateOptionsMenu(menu);
    }

    private void listAllDishes(){
        serverCalls.getAllDishes(new DishCallback() {
            @Override
            public void gotAllDishes(List<Map<String, Object>> dishesList) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new DishListFragment());
                fragmentTransaction.commit();

                DishesViewModel viewModel = new ViewModelProvider(MainDrawerActivity.this).get(DishesViewModel.class);
                viewModel.getDishListRecyclerView().observe(MainDrawerActivity.this,item ->{
                    item.setLayoutManager(new LinearLayoutManager(MainDrawerActivity.this));
                    DishPreviewCardAdapter adapter = new DishPreviewCardAdapter(MainDrawerActivity.this,dishesList);
                    item.setAdapter(adapter);

                    SearchView searchView = (SearchView) searchFilter.getActionView();
                    searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

                    searchFilter.setVisible(true);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                });
            }
        },userId);
    }
}
