package com.Lagosa.homemanager_app.ui.Polls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import java.util.UUID;

public class CreatePollFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_poll,container,false);

        EditText message = view.findViewById(R.id.pollCreateMessage);
        Button create = view.findViewById(R.id.pollCreateBtn);

        Bundle bundle = getArguments();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCalls serverCalls = new ServerCalls(inflater.getContext());
                serverCalls.createPoll(UUID.fromString(bundle.getString("userId")),message.getText().toString());

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new PollListFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
