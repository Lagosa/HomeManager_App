package com.Lagosa.homemanager_app.ui.Dishes;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DishPreviewCardAdapter extends RecyclerView.Adapter<DishPreviewCardAdapter.ViewHolder> implements Filterable {

    private  List<Map<String,Object>> fullDishList;
    private final List<Map<String,Object>> dishList;

    private final LayoutInflater inflater;

    public DishPreviewCardAdapter(Context context, List<Map<String, Object>> dishList) {
        this.dishList = dishList;
        this.inflater = LayoutInflater.from(context);
        fullDishList = new ArrayList<>(dishList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dish_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText((String)dishList.get(position).get("name"));
        holder.type.setText((String)dishList.get(position).get("type"));
        holder.visibility.setText((String)dishList.get(position).get("visibility"));
        holder.nrTimesMade.setText((String)dishList.get(position).get("nrTimesMade"));
        holder.dishPosition.setText(position+"");

        List<Map<String,Object>> ingredientList = (List) dishList.get(position).get("ingredients");
        holder.nrIngredients.setText(ingredientList.size()+"");

        holder.markDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCalls serverCalls = new ServerCalls(inflater.getContext());
                serverCalls.markDone(Integer.parseInt((String)dishList.get(Integer.parseInt(holder.dishPosition.getText().toString())).get("id")));
            }
        });

        holder.modifyVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String visibility = "";
                if(holder.visibility.getText().equals("PRIVATE")){
                    visibility = "PUBLIC";
                }else{
                    visibility = "PRIVATE";
                }

                ServerCalls serverCalls = new ServerCalls(inflater.getContext());
                serverCalls.modifyVisibility(Integer.parseInt((String)dishList.get(Integer.parseInt(holder.dishPosition.getText().toString())).get("id")),visibility);
            }
        });

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(inflater.getContext());
                View detailPopupView = inflater.inflate(R.layout.dish_details,null);

                TextView detailName = (TextView) detailPopupView.findViewById(R.id.dishDetailName);
                TextView detailType = (TextView) detailPopupView.findViewById(R.id.dishDetailType);
                TextView detailRecipe = (TextView) detailPopupView.findViewById(R.id.dishDetailRecipe);
                RecyclerView ingredientContainer  = (RecyclerView) detailPopupView.findViewById(R.id.dishDetailIngredientContainer);

                dialogBuilder.setView(detailPopupView);
                dialog = dialogBuilder.create();
                dialog.show();

                detailName.setText(holder.name.getText().toString());
                detailType.setText(holder.type.getText().toString());
                detailRecipe.setText((String)dishList.get(Integer.parseInt(holder.dishPosition.getText().toString())).get("recipe"));

                ingredientContainer.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
                DishDetailIngredientsCardAdapter ingredientAdapter = new DishDetailIngredientsCardAdapter(inflater.getContext(),(ArrayList) dishList.get(Integer.parseInt(holder.dishPosition.getText().toString())).get("ingredients"));
                ingredientContainer.setAdapter(ingredientAdapter);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    @Override
    public Filter getFilter() {
        return search_filter;
    }

    private Filter search_filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Map<String,Object>> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(fullDishList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Map<String,Object> item : fullDishList){
                    if(item.get("name").toString().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dishList.clear();
            dishList.addAll((List<Map<String,Object>>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, type,visibility,nrTimesMade,nrIngredients,dishPosition;
        ConstraintLayout markDone, modifyVisibility;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dishPreviewTitle);
            type = itemView.findViewById(R.id.dishPreviewType);
            visibility = itemView.findViewById(R.id.dishPreviewVisibility);
            nrTimesMade = itemView.findViewById(R.id.dishPreviewNrTimesMade);
            nrIngredients = itemView.findViewById(R.id.dishPreviewNrIngredients);
            markDone = itemView.findViewById(R.id.dishPreviewMarkDone);
            modifyVisibility = itemView.findViewById(R.id.dishPreviewChangeVisibility);
            dishPosition = itemView.findViewById(R.id.dishCardPosition);
            container = itemView.findViewById(R.id.dishPreviewContainer);
        }
    }


}
