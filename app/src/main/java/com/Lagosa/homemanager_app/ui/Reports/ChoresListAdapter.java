package com.Lagosa.homemanager_app.ui.Reports;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.R;

import java.util.List;
import java.util.Map;

public class ChoresListAdapter  extends RecyclerView.Adapter<ChoresListAdapter.ViewHolder> {

    private final List<Map<String,Object>> choreList;
    public ChoresListAdapter(List<Map<String, Object>> choreList) {
        this.choreList = choreList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card_chore,parent,false);
        return new ChoresListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.w("GetReport","" + choreList.get(position).get("title"));
        holder.choreTitle.setText((String) choreList.get(position).get("title"));
        holder.choreDeadline.setText(choreList.get(position).get("deadline").toString());
    }

    @Override
    public int getItemCount() {
        return choreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView choreTitle, choreDeadline;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            choreTitle = itemView.findViewById(R.id.reportChoreTitle);
            choreDeadline = itemView.findViewById(R.id.reportChoreDeadline);
        }
    }
}
