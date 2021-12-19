package com.Lagosa.homemanager_app.ui.Chores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.ViewModels.ChoreViewModel;

public class MyChoresListFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.view_mychores_fragment,container,false);

        ChoreViewModel viewModel = new ViewModelProvider(requireActivity()).get(ChoreViewModel.class);
        viewModel.setMyChoresList(myView.findViewById(R.id.myChoresList));

        return myView;
    }
}
