package com.Lagosa.homemanager_app.ui.Reports;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.R;

import java.util.List;
import java.util.Map;

public class ReportCardAdapter extends RecyclerView.Adapter<ReportCardAdapter.ViewHolder> {

    private final List<Report> reportList;
    private final LayoutInflater inflater;
    private final Activity activity;

    public ReportCardAdapter(Activity activity,Context context, List<Report> reportList){
        this.reportList = reportList;
        inflater = LayoutInflater.from(context);
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.report_card_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(reportList.get(position).getUserName());
        holder.nrChoresDone.setText(reportList.get(position).getNrDone()+"");
        holder.nrChoresNotDone.setText(reportList.get(position).getNrNotDone()+"");

        createChoreList(holder.choresDone,reportList.get(position).getListDone());

        createChoreList(holder.choresNotDone,reportList.get(position).getListNotDone());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView username, nrChoresDone, nrChoresNotDone;
        RecyclerView choresDone, choresNotDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.reportUsername);
            nrChoresDone = itemView.findViewById(R.id.reportNrDone);
            nrChoresNotDone = itemView.findViewById(R.id.reportNrNotDone);
            choresDone = itemView.findViewById(R.id.reportDoneList);
            choresNotDone = itemView.findViewById(R.id.reportNotDoneList);
        }
    }

    private void createChoreList(RecyclerView recyclerView,List<Map<String,Object>> chores){
        ChoresListAdapter choresListAdapter = new ChoresListAdapter(chores);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(choresListAdapter);
    }
}
