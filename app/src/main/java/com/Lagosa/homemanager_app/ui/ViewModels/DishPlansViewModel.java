package com.Lagosa.homemanager_app.ui.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.util.Map;

public class DishPlansViewModel extends ViewModel {
    private MutableLiveData<RecyclerView> planContainer = new MutableLiveData<>();
    private MutableLiveData<Map<String, Date>> dateInterval = new MutableLiveData<>();

    public LiveData<RecyclerView> getPlanContainer() {
        return planContainer;
    }

    public void setPlanContainer(RecyclerView planContainer) {
        this.planContainer.setValue(planContainer);
    }

    public LiveData<Map<String, Date>> getDateInterval() {
        return dateInterval;
    }

    public void setDateInterval(Map<String, Date> plannedDishListView) {
        Log.w("DishPlan","Setting date interval");
        this.dateInterval.setValue(plannedDishListView);
    }
}
