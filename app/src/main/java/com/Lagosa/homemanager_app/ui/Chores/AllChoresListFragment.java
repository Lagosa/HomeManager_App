package com.Lagosa.homemanager_app.ui.Chores;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.ViewModels.ChoreViewModel;

public class AllChoresListFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup myView = (ViewGroup) inflater.inflate(R.layout.view_chore_fragment,container,false);

        ChoreViewModel viewModel = new ViewModelProvider(requireActivity()).get(ChoreViewModel.class);
        viewModel.setNotDoneListRecycleViewFamily(myView.findViewById(R.id.choresList));

        Button btnCreateChore = myView.findViewById(R.id.addChorebtn);
        btnCreateChore.setOnClickListener(this);

        Button btnChangeDeadline = myView.findViewById(R.id.changeDeadlinebtn);
        btnChangeDeadline.setOnClickListener(this);

        return myView;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (v.getId()){
            case R.id.addChorebtn:
                fragmentTransaction.replace(R.id.container_fragment,new CreateChoreFragment());
                break;
            case R.id.changeDeadlinebtn:
                Toast.makeText(getContext(),"Change button",Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(),"Default button",Toast.LENGTH_LONG).show();
        }

        fragmentTransaction.commit();
    }
}
