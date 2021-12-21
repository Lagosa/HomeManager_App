package com.Lagosa.homemanager_app.ui.Polls;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.Database.DishCallback;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PollCardAdapter extends RecyclerView.Adapter<PollCardAdapter.ViewHolder> {

    private final List<Map<String,Object>> pollItems;
    private LayoutInflater inflater;
    private final UUID userId;
    private ArrayAdapter<String> dishNamesAdapter;
    private String dishSelected;
    private List<String> dishNameList;
    private List<Map<String,Object>> dishList;
    private final boolean isClosed;

    public PollCardAdapter(Context context, List<Map<String, Object>> pollItems,Boolean isClosed) {
        this.pollItems = pollItems;
        inflater = LayoutInflater.from(context);
        DatabaseHelper dbHelper = new DatabaseHelper(inflater.getContext());
        Cursor data = dbHelper.getUserId();
        data.moveToFirst();
        userId = UUID.fromString(data.getString(0));
        this.isClosed = isClosed;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.poll_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(isClosed){
            holder.addDishBtn.setVisibility(View.GONE);
        }

        holder.message.setText(pollItems.get(position).get("message").toString());
        holder.pollItemPosition.setText(position+"");

        List<Map<String,Object>> dishes = new ArrayList<>();
        JSONArray dishJsonArray = (JSONArray) pollItems.get(position).get("dishes");
        for(int i = 0; i< dishJsonArray.length(); i++){
            try {
                Map<String, Object> dish = new HashMap<>();
                JSONObject dishObject = dishJsonArray.getJSONObject(i);
                dish.put("dishName",dishObject.getString("dishName"));
                dish.put("id",dishObject.getString("id"));
                dish.put("forList",dishObject.get("forList"));
                dish.put("againstList",dishObject.get("againstList"));
                dishes.add(dish);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.w("pollList",""+dishes);

        holder.dishListContainer.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        PollDishCardAdapter cardAdapter = new PollDishCardAdapter(inflater.getContext(), dishes,userId,isClosed);
        holder.dishListContainer.setAdapter(cardAdapter);

        holder.addDishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(inflater.getContext());
                View detailPopupView = inflater.inflate(R.layout.poll_add_dish,null);

                dialogBuilder.setView(detailPopupView);
                dialog = dialogBuilder.create();
                dialog.show();

                Spinner dishes = detailPopupView.findViewById(R.id.spnPollAddDishName);

                ServerCalls serverCalls = new ServerCalls(inflater.getContext());
                serverCalls.getAllDishes(new DishCallback() {
                    @Override
                    public void gotAllDishes(List<Map<String, Object>> dishesList) {
                        dishList = dishesList;
                        dishNameList = new ArrayList<>();
                        for(Map<String,Object> dish : dishesList){
                            dishNameList.add(dish.get("name").toString());
                        }
                        dishNamesAdapter = new ArrayAdapter<>(inflater.getContext(), R.layout.spinner_item,dishNameList);
                        dishNamesAdapter.setDropDownViewResource(R.layout.spinner_item);
                        dishes.setAdapter(dishNamesAdapter);
                    }
                },userId);

                dishes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        dishSelected = dishes.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Button addDishBtn = detailPopupView.findViewById(R.id.pollAddDishAddBTN);
                addDishBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String dishId = dishList.get(dishNameList.indexOf(dishSelected)).get("id").toString();
                        serverCalls.addDishToPoll(userId,pollItems.get(Integer.parseInt(holder.pollItemPosition.getText().toString())).get("id").toString(),dishId);
                    }
                });
            }
        });

        holder.changeVisibilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCalls serverCalls = new ServerCalls(inflater.getContext());
                String status = pollItems.get(Integer.parseInt(holder.pollItemPosition.getText().toString())).get("status").toString();
                if(status.equals("OPEN")){
                    status = "CLOSED";
                }else{
                    status = "OPEN";
                }
                serverCalls.changePollStatus(userId,pollItems.get(Integer.parseInt(holder.pollItemPosition.getText().toString())).get("id").toString(),status);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pollItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message,pollItemPosition;
        ConstraintLayout addDishBtn, changeVisibilityBtn;
        RecyclerView dishListContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.pollMessage);
            addDishBtn = itemView.findViewById(R.id.pollAddDish);
            changeVisibilityBtn = itemView.findViewById(R.id.pollChangeVisibility);
            dishListContainer = itemView.findViewById(R.id.dishListContainer);
            pollItemPosition = itemView.findViewById(R.id.pollItemPosition);
        }
    }
}
