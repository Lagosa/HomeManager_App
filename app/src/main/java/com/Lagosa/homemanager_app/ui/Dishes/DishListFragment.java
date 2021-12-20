package com.Lagosa.homemanager_app.ui.Dishes;

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
import com.Lagosa.homemanager_app.ui.ViewModels.DishesViewModel;

public class DishListFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.view_dishes_fragment,container,false);

        DishesViewModel viewModel = new ViewModelProvider(requireActivity()).get(DishesViewModel.class);
        viewModel.setDishListRecyclerView(myView.findViewById(R.id.listAllDishesRecyclerView));

        Button newDishBtn = myView.findViewById(R.id.createDish);
        newDishBtn.setOnClickListener(this);

        Button randomDishBtn = myView.findViewById(R.id.getRandomDish);
        randomDishBtn.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch(v.getId()){
            case R.id.createDish:
                fragmentTransaction.replace(R.id.container_fragment,new CreateDishFragment());
                break;
            case R.id.getRandomDish:
                fragmentTransaction.replace(R.id.container_fragment,new RandomDishFragment());
                break;
        }

        fragmentTransaction.commit();

    }
}
