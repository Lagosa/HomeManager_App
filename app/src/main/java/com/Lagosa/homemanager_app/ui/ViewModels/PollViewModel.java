package com.Lagosa.homemanager_app.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

public class PollViewModel extends ViewModel {
    private MutableLiveData<Map<String,RecyclerView>> pollContainer = new MutableLiveData<>();

    public LiveData<Map<String,RecyclerView>> getPollContainer() {
        return pollContainer;
    }

    public void setPollContainer(Map<String,RecyclerView> pollContainer) {
        this.pollContainer.setValue(pollContainer);
    }
}
