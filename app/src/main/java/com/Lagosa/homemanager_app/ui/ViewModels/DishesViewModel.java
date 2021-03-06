package com.Lagosa.homemanager_app.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public class DishesViewModel extends ViewModel {
    MutableLiveData<RecyclerView> dishListRecyclerView = new MutableLiveData<>();
    MutableLiveData<String> dayToGetIngredientsFrom = new MutableLiveData<>();

    public LiveData<RecyclerView> getDishListRecyclerView() {
        return dishListRecyclerView;
    }

    public void setDishListRecyclerView(RecyclerView dishListRecyclerView) {
        this.dishListRecyclerView.setValue(dishListRecyclerView);
    }

    public LiveData<String> getDayToGetIngredientsFrom() {
        return dayToGetIngredientsFrom;
    }

    public void setDataFromFragment(String data) {
        this.dayToGetIngredientsFrom.setValue(data);
    }
}
