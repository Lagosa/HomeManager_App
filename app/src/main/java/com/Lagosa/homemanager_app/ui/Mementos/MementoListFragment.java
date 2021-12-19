package com.Lagosa.homemanager_app.ui.Mementos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.ViewModels.MementoViewModel;

public class MementoListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.view_mementos_fragment,container,false);

        MementoViewModel viewModel = new ViewModelProvider(requireActivity()).get(MementoViewModel.class);
        viewModel.setRecyclerViewMutableLiveData(myView.findViewById(R.id.mementosList));

        Button createMemento = myView.findViewById(R.id.addMementoBTN);
        createMemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new CreateMementoFragment());
                fragmentTransaction.commit();
            }
        });

        return myView;
    }
}
