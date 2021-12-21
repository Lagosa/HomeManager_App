package com.Lagosa.homemanager_app.ui.Polls;

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
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.ViewModels.PollViewModel;

import java.util.HashMap;
import java.util.Map;

public class PollListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.view_polls_fragment,container,false);

        Button create = myView.findViewById(R.id.pollListCreate);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId",getArguments().getString("userId"));

                CreatePollFragment createPollFragment = new CreatePollFragment();
                createPollFragment.setArguments(bundle);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,createPollFragment);
                fragmentTransaction.commit();
            }
        });

        PollViewModel viewModel = new ViewModelProvider(requireActivity()).get(PollViewModel.class);
        Map<String, RecyclerView> pollContainers = new HashMap<>();
        pollContainers.put("open",myView.findViewById(R.id.pollOpenPollContainer));
        pollContainers.put("closed",myView.findViewById(R.id.pollClosedPollContainer));

        viewModel.setPollContainer(pollContainers);

        return myView;
    }
}
