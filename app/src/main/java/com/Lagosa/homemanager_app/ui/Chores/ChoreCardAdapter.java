package com.Lagosa.homemanager_app.ui.Chores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import java.util.List;
import java.util.UUID;

public class ChoreCardAdapter extends RecyclerView.Adapter<ChoreCardAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Chore> chores;
    UUID userId;

    public ChoreCardAdapter(Context context, List<Chore> chores, UUID userId){
        this.inflater = LayoutInflater.from(context);
        this.chores = chores;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ChoreCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chore_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoreCardAdapter.ViewHolder holder, int position) {
        holder.choreTitle.setText(chores.get(position).getTitle());
        holder.choreDeadline.setText(chores.get(position).getDeadline().toString());
        holder.choreType.setText(chores.get(position).getTypeName());
        holder.choreSubmitter.setText(chores.get(position).getSubmittedByName());
        holder.choreAssignedTo.setText(chores.get(position).getDoneByName());
        holder.choreDescription.setText(chores.get(position).getDescription());

        holder.btnDeleteChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("DeleteChore","Onclick event recorded!");
                ServerCalls serverCalls = new ServerCalls(inflater.getContext());

                Log.w("DeleteChore","User data got! Chore: " + chores.get(holder.getAdapterPosition()).getId() + " User: " + userId);
                serverCalls.deleteChore(chores.get(holder.getAdapterPosition()).getId(), userId);
            }
        });

        holder.btnGetChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCalls serverCalls = new ServerCalls((inflater.getContext()));
                serverCalls.takeUpChore(chores.get(holder.getAdapterPosition()).getId(),userId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView choreTitle, choreDeadline, choreType, choreSubmitter, choreAssignedTo,choreDescription;
        ConstraintLayout btnDeleteChore, btnGetChore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            choreTitle = itemView.findViewById(R.id.choreTitle);
            choreDeadline = itemView.findViewById(R.id.choreDeadline);
            choreType = itemView.findViewById(R.id.choreType);
            choreSubmitter = itemView.findViewById(R.id.choreSubmitter);
            choreAssignedTo = itemView.findViewById(R.id.choreAssignedTo);
            choreDescription = itemView.findViewById(R.id.choreDescription);

            btnDeleteChore = itemView.findViewById(R.id.choreCardDeleteBTN);
            btnGetChore = itemView.findViewById(R.id.choreCardGetBTN);

        }
    }
}
