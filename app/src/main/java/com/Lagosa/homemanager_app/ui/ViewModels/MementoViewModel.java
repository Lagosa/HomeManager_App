package com.Lagosa.homemanager_app.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public class MementoViewModel extends ViewModel {
    private MutableLiveData<RecyclerView> recyclerViewMutableLiveData = new MutableLiveData<>();

    public LiveData<RecyclerView> getRecyclerViewMutableLiveData() {
        return recyclerViewMutableLiveData;
    }

    public void setRecyclerViewMutableLiveData(RecyclerView recyclerView) {
        this.recyclerViewMutableLiveData.setValue(recyclerView);
    }
}
