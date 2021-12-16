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
import com.Lagosa.homemanager_app.ui.ViewModels.ChoreListViewModel;

public class AllChoresListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup myView = (ViewGroup) inflater.inflate(R.layout.view_chore_fragment,container,false);

        ChoreListViewModel viewModel = new ViewModelProvider(requireActivity()).get(ChoreListViewModel.class);

        viewModel.setNotDoneListRecycleViewFamily(myView.findViewById(R.id.choresList));

        return myView;
    }
}
