package com.Lagosa.homemanager_app.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public class ChoreViewModel extends ViewModel {
    private MutableLiveData<RecyclerView> notDoneListRecycleViewFamily = new MutableLiveData<>();

    public void setNotDoneListRecycleViewFamily(RecyclerView view){
        notDoneListRecycleViewFamily.setValue(view);
    }

    public LiveData<RecyclerView> getNotDoneListRecycleViewFamily(){
        return notDoneListRecycleViewFamily;
    }
}
