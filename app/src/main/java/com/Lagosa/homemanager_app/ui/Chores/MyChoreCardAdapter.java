package com.Lagosa.homemanager_app.ui.Chores;

import android.content.Context;
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

public class MyChoreCardAdapter extends RecyclerView.Adapter<MyChoreCardAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Chore> myChores;
    private final UUID userId;

    public MyChoreCardAdapter(Context context, List<Chore> myChores, UUID userId){
        this.inflater = LayoutInflater.from(context);
        this.myChores = myChores;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MyChoreCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mychores_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChoreCardAdapter.ViewHolder holder, int position) {
        if(myChores.size() > 0) {
            holder.choreTitle.setText(myChores.get(position).getTitle());
            holder.choreDescription.setText(myChores.get(position).getDescription());
            holder.choreType.setText(myChores.get(position).getTypeName());
            holder.choreSubmitter.setText(myChores.get(position).getSubmittedByName());
            holder.choreAssignedTo.setText(myChores.get(position).getDoneByName());
            holder.choreDeadline.setText(myChores.get(position).getDeadline().toString());

            holder.btnDoneChore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ServerCalls serverCalls = new ServerCalls(inflater.getContext());
                    serverCalls.markChoreAsDone(myChores.get(holder.getAdapterPosition()).getId(), userId);
                }
            });
        }else{
            holder.choreContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(myChores.size() > 0){
            return myChores.size();
        }else{
            return 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView choreTitle, choreDeadline, choreType, choreSubmitter, choreAssignedTo,choreDescription,noChoresTxt;
        ConstraintLayout btnDoneChore;
        ConstraintLayout choreContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            choreContainer = itemView.findViewById(R.id.myChoreContainer);
            noChoresTxt = itemView.findViewById(R.id.myChoresNoChores);
            choreTitle = itemView.findViewById(R.id.myChoreTitle);
            choreDeadline = itemView.findViewById(R.id.myChoreDeadline);
            choreType = itemView.findViewById(R.id.myChoreType);
            choreSubmitter = itemView.findViewById(R.id.myChoreSubmitter);
            choreAssignedTo = itemView.findViewById(R.id.myChoreAssignedTo);
            choreDescription = itemView.findViewById(R.id.myChoreDescription);

            btnDoneChore = itemView.findViewById(R.id.choreCardDoneBTN);
        }
    }
}
