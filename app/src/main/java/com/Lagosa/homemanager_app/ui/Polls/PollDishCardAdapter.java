package com.Lagosa.homemanager_app.ui.Polls;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PollDishCardAdapter extends RecyclerView.Adapter<PollDishCardAdapter.ViewHolder> {

    private final List<Map<String,Object>> dishData;
    private LayoutInflater inflater;
    private final UUID userId;
    private final boolean isClosed;

    public PollDishCardAdapter(Context context, List<Map<String, Object>> dishData, UUID userId,Boolean isClosed) {
        this.dishData = dishData;
        inflater = LayoutInflater.from(context);
        this.userId = userId;
        this.isClosed = isClosed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.poll_dish_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(isClosed){
            holder.voteAgainst.setVisibility(View.GONE);
            holder.voteFor.setVisibility(View.GONE);
        }

        holder.dishName.setText(dishData.get(position).get("dishName").toString());
        holder.pollDishId.setText(dishData.get(position).get("id").toString());

        Log.w("pollList",dishData.get(position).toString());

        holder.voteFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCalls serverCalls = new ServerCalls(inflater.getContext());
                serverCalls.pollVote(userId,holder.pollDishId.getText().toString(),"FOR");
            }
        });

        holder.voteAgainst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCalls serverCalls = new ServerCalls(inflater.getContext());
                serverCalls.pollVote(userId,holder.pollDishId.getText().toString(),"AGAINST");
            }
        });

        List<String> usersFor = getUserNamesFromArrayList("forList",position);
        List<String> usersAgainst = getUserNamesFromArrayList("againstList",position);

        Log.w("pollList","" + usersFor);
        Log.w("pollList","" + usersAgainst);


        holder.forList.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        PollDishUserCardAdapter forCardAdapter = new PollDishUserCardAdapter(inflater.getContext(), usersFor);
        holder.forList.setAdapter(forCardAdapter);

        holder.againstList.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        PollDishUserCardAdapter againstCardAdapter = new PollDishUserCardAdapter(inflater.getContext(),usersAgainst);
        holder.againstList.setAdapter(againstCardAdapter);
    }

    private List<String> getUserNamesFromArrayList(String listType,int position){
        List<String> users = new ArrayList<>();
        JSONArray usersArray = (JSONArray) dishData.get(position).get(listType);
        for(int i = 0 ;i < usersArray.length(); i++){
            try {
                JSONObject userObject = usersArray.getJSONObject(i);
                users.add(userObject.get("userName").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public int getItemCount() {
        return dishData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishName,pollDishId;
        ConstraintLayout voteFor, voteAgainst;
        RecyclerView forList, againstList;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.pollDishName);
            voteFor = itemView.findViewById(R.id.pollVoteFor);
            voteAgainst = itemView.findViewById(R.id.pollVoteAgainst);
            forList = itemView.findViewById(R.id.pollForList);
            againstList = itemView.findViewById(R.id.pollAgainstList);
            pollDishId = itemView.findViewById(R.id.dishPollId);
        }
    }
}
