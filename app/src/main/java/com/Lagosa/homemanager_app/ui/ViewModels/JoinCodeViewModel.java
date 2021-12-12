package com.Lagosa.homemanager_app.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JoinCodeViewModel extends ViewModel {
    private final MutableLiveData<Integer> joinCode = new MutableLiveData<Integer>();

    public void setJoinCode(Integer joinCode){
        this.joinCode.setValue(joinCode);
    }

    public LiveData<Integer> getJoinCode(){
        return joinCode;
    }
}
