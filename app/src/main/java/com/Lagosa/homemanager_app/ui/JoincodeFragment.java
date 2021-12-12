package com.Lagosa.homemanager_app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.ViewModels.JoinCodeViewModel;

public class JoincodeFragment extends Fragment {

    ViewGroup myView;
    TextView joinCodeTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = (ViewGroup) inflater.inflate(R.layout.joincode_fragment,container,false);
        joinCodeTxt = myView.findViewById(R.id.txt_joinCode);
        JoinCodeViewModel viewModel = new ViewModelProvider(requireActivity()).get(JoinCodeViewModel.class);
        viewModel.getJoinCode().observe(getViewLifecycleOwner(), item -> {
            joinCodeTxt.setText(item+"");
        });
        return myView;
    }
}
