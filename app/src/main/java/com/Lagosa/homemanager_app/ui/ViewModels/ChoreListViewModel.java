package com.Lagosa.homemanager_app.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.ui.Chores.Chore;

import java.util.List;

public class ChoreListViewModel extends ViewModel {
    private MutableLiveData<List<Chore>> notDoneChoresFamily = new MutableLiveData<List<Chore>>();
    private MutableLiveData<RecyclerView> notDoneListRecycleViewFamily = new MutableLiveData<>();

    public void setNotDoneFamilyChoreList(List<Chore> choreList){
        this.notDoneChoresFamily.setValue(choreList);
    }

    public LiveData<List<Chore>> getNotDoneFamilyChoreList(){
        return notDoneChoresFamily;
    }

    public void setNotDoneListRecycleViewFamily(RecyclerView view){
        notDoneListRecycleViewFamily.setValue(view);
    }

    public LiveData<RecyclerView> getNotDoneListRecycleViewFamily(){
        return notDoneListRecycleViewFamily;
    }
}
