package com.Lagosa.homemanager_app.ui.Dishes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        List<Map<String,Object>> ingredientList = (List) dishList.get(position).get("ingredients");
        holder.nrIngredients.setText(ingredientList.size()+"");

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

        TextView name, type,visibility,nrTimesMade,nrIngredients;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dishPreviewTitle);
            type = itemView.findViewById(R.id.dishPreviewType);
            visibility = itemView.findViewById(R.id.dishPreviewVisibility);
            nrTimesMade = itemView.findViewById(R.id.dishPreviewNrTimesMade);
            nrIngredients = itemView.findViewById(R.id.dishPreviewNrIngredients);
        }
    }


}
